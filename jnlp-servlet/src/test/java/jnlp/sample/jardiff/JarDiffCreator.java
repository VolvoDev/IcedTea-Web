package jnlp.sample.jardiff;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class was used to create the JarDiff files that are used in unit tests.
 */
public class JarDiffCreator {

    public static void main(String[] args) throws Exception {

        final Path version1 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-1.jar");
        final Path version2 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-2.jar");
        final Path version3 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-3.jar");
        final Path version4 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-4.jar");
        final Path version5 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-5.jar");
        final Path version6 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-6.jar");
        final Path version7 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-7.jar");
        final Path version8 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "version-8.jar");


        final Path diff_1_to_2 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "diff-1-to-2.jardiff");
        final Path diff_2_to_3 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "diff-2-to-3.jardiff");
        final Path diff_3_to_4 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "diff-3-to-4.jardiff");
        final Path diff_4_to_5 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "diff-4-to-5.jardiff");
        final Path diff_5_to_6 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "diff-5-to-6.jardiff");
        final Path diff_6_to_7 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "diff-6-to-7.jardiff");
        final Path diff_7_to_8 = Paths.get("/Users/hendrikebbers/Desktop/JarDiff", "diff-7-to-8.jardiff");


       // JarDiff.main(new String[]{"-debug", "-creatediff", "-output", diff_1_to_2.toString(), version1.toString(), version2.toString()});
       // JarDiff.main(new String[]{"-debug", "-creatediff", "-output", diff_2_to_3.toString(), version2.toString(), version3.toString()});
       // JarDiff.main(new String[]{"-debug", "-creatediff", "-output", diff_3_to_4.toString(), version3.toString(), version4.toString()});
       // JarDiff.main(new String[]{"-debug", "-creatediff", "-output", diff_4_to_5.toString(), version4.toString(), version5.toString()});
       // JarDiff.main(new String[]{"-debug", "-creatediff", "-output", diff_5_to_6.toString(), version5.toString(), version6.toString()});
       // JarDiff.main(new String[]{"-debug", "-creatediff", "-output", diff_6_to_7.toString(), version6.toString(), version7.toString()});
       // JarDiff.main(new String[]{"-debug", "-creatediff", "-output", diff_7_to_8.toString(), version7.toString(), version8.toString()});
    }
}
