/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kordamp.gradle.markdown

import org.apache.commons.io.FileUtils

/**
 * @author Andres Almiray
 */
class MarkdownWorkerImpl implements MarkdownWorker {
    void process(Conversion conversion, File sourceDir, File outputDir, Map configuration) {
        outputDir.mkdirs()

        sourceDir.eachFileRecurse { file ->
            if (file.directory) {
                outputDirFor(file, sourceDir.absolutePath, outputDir)
            } else {
                File destinationParentDir = outputDirFor(file, sourceDir.absolutePath, outputDir)
                if (conversion.accept(file)) {
                    String content = conversion.convert(file, configuration)
                    File target = new File("${destinationParentDir}/${stripFilenameExtension(file.name)}${conversion.targetExtension()}")
                    target.text = content
                } else {
                    File target = new File("${destinationParentDir}/${file.name}")
                    target.withOutputStream { it << file.newInputStream() }
                }
            }
        }
    }

    private static File outputDirFor(File source, String basePath, File outputDir) {
        String filePath = source.directory ? source.absolutePath : source.parentFile.absolutePath
        String relativeFilePath = filePath - basePath
        File destinationParentDir = new File("${outputDir}/${relativeFilePath}")
        if (!destinationParentDir.exists()) destinationParentDir.mkdirs()
        destinationParentDir
    }

    /**
     * Strip the filename extension from the given path,
     * e.g. "mypath/myfile.txt" -> "mypath/myfile".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the path with stripped filename extension,
     *         or <code>null</code> if none
     */
    private static String stripFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int extIndex = path.lastIndexOf(".");
        if (extIndex == -1) {
            return path;
        }
        int folderIndex = path.lastIndexOf("/");
        if (folderIndex > extIndex) {
            return path;
        }
        return path.substring(0, extIndex);
    }
}
