package net.adoptopenjdk.icedteaweb.classloader;

import net.adoptopenjdk.icedteaweb.jnlp.element.resource.JARDesc;
import net.adoptopenjdk.icedteaweb.xmlparser.ParseException;
import net.sourceforge.jnlp.JNLPFileFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class JnlpApplicationClassLoaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void findClass1() throws Exception {

        //given
        final List<Part> parts = createFor("empty.jnlp").getParts();
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //expect
        thrown.expect(ClassNotFoundException.class);
        thrown.expectMessage("not.in.Classpath");

        //when
        final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(partsHandler);
        classLoader.findClass("not.in.Classpath");
    }

    @Test
    public void findClass3() throws Exception {

        //given
        final List<Part> parts = createFor("unavailable-jar.jnlp").getParts();
        final ErrorJarProvider jarProvider = new ErrorJarProvider();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        // expect
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Error while downloading jar!");

        //when
        new JnlpApplicationClassLoader(partsHandler);
    }

    @Test
    public void findClass4() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("eager-and-lazy.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        new JnlpApplicationClassLoader(partsHandler);

        //than
        Assert.assertTrue(jarProvider.hasTriedToDownload("eager.jar"));
        Assert.assertFalse(jarProvider.hasTriedToDownload("lazy.jar"));
    }

    @Test
    public void findClass5() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("eager-and-lazy.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        try {
            final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(partsHandler);
            classLoader.findClass("class.in.lazy.Package");
        } catch (final Exception ignore) {}

        //than
        Assert.assertTrue(jarProvider.hasTriedToDownload("eager.jar"));
        Assert.assertTrue(jarProvider.hasTriedToDownload("lazy.jar"));
    }

    @Test
    public void findClass6() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("lazy-not-recursive.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        new JnlpApplicationClassLoader(partsHandler);

        //than
        Assert.assertEquals(0, jarProvider.getDownloaded().size());
    }

    @Test
    public void findClass7() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("lazy-not-recursive.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        try {
            final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(partsHandler);
            classLoader.findClass("class.in.lazy.A");
        } catch (final Exception ignore) {}

        //than
        Assert.assertEquals(1, jarProvider.getDownloaded().size());
    }

    @Test
    public void findClass8() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("lazy-not-recursive.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        try {
            final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(partsHandler);
            classLoader.findClass("class.in.lazy.sub.A");
        } catch (final Exception ignore) {}

        //than
        Assert.assertEquals(1, jarProvider.getDownloaded().size());
    }

    @Test
    public void findClass9() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("lazy-recursive.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        new JnlpApplicationClassLoader(partsHandler);

        //than
        Assert.assertEquals(0, jarProvider.getDownloaded().size());
    }

    @Test
    public void findClass10() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("lazy-recursive.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        try {
            final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(partsHandler);
            classLoader.findClass("class.in.lazy.A");
        } catch (final Exception ignore) {}

        //than
        Assert.assertEquals(1, jarProvider.getDownloaded().size());
    }

    @Test
    public void findClass11() throws Exception {

        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("lazy-recursive.jnlp").getParts();
        final PartsHandler partsHandler = new PartsHandler(parts, jarProvider);

        //when
        try {
            final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(partsHandler);
            classLoader.findClass("class.in.lazy.sub.A");
        } catch (final Exception ignore) {}

        //than
        Assert.assertEquals(1, jarProvider.getDownloaded().size());
    }


    private static class DummyJarProvider implements Function<JARDesc, URL> {

        private final List<JARDesc> downloaded = new CopyOnWriteArrayList<>();

        @Override
        public URL apply(final JARDesc jarDesc) {
            System.out.println("Should load " + jarDesc.getLocation());
            downloaded.add(jarDesc);
            return jarDesc.getLocation();
        }

        public boolean hasTriedToDownload(final String name) {
            return downloaded.stream()
                    .anyMatch(jar -> jar.getLocation().toString().endsWith(name));
        }

        public List<JARDesc> getDownloaded() {
            return Collections.unmodifiableList(downloaded);
        }
    }

    private static class ErrorJarProvider implements Function<JARDesc, URL> {

        @Override
        public URL apply(final JARDesc jarDesc) {
            throw new RuntimeException("Can not download " + jarDesc.getLocation());
        }

    }

    public static JarExtractor createFor(final String name) throws IOException, ParseException {
        final JNLPFileFactory jnlpFileFactory = new JNLPFileFactory();
        return new JarExtractor(jnlpFileFactory.create(JnlpApplicationClassLoaderTest.class.getResource(name)), jnlpFileFactory);
    }

}
