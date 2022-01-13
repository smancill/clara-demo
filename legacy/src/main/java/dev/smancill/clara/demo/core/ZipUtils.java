/*
 * SPDX-FileCopyrightText: © Sebastián Mancilla
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.smancill.clara.demo.core;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

final class ZipUtils {

    private ZipUtils() { }

    static Path extract(Path dataset) throws IOException {
        var unzipDir = Files.createTempDirectory("dataset");
        try {
            var zipFile = new ZipFile(dataset.toString());
            zipFile.extractAll(unzipDir.toString());
            return unzipDir;
        } catch (ZipException e) {
            throw new IOException(e);
        }
    }

    static void compress(Path directory, Path dataset) throws IOException {
        try {
            var zipFile = new ZipFile(dataset.toString());
            zipFile.addFolder(directory.toFile(), new ZipParameters());
        } catch (ZipException e) {
            throw new IOException(e);
        }
    }

    static void removeDirectory(Path dir) throws IOException {
        Files.walk(dir, FileVisitOption.FOLLOW_LINKS)
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }
}
