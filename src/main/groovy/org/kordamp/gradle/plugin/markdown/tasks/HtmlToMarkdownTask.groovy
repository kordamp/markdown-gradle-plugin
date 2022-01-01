/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2013-2022 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.gradle.plugin.markdown.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.nio.charset.StandardCharsets

/**
 * @author Andres Almiray
 */
class HtmlToMarkdownTask extends DefaultTask {
    @InputDirectory File sourceDir
    @OutputDirectory File outputDir
    @Input Map configuration = [:]

    @Optional @Input String inputEncoding = StandardCharsets.UTF_8.displayName()
    @Optional @Input String outputEncoding = StandardCharsets.UTF_8.displayName()

    private MarkdownWorker worker

    HtmlToMarkdownTask() {
        sourceDir = project.file('src/html')
        outputDir = new File(project.buildDir, 'gen-markdown')
        worker = new MarkdownWorkerImpl()
    }

    @TaskAction
    void runTask() {
        Map options = [
            sourceDir     : sourceDir,
            outputDir     : outputDir,
            inputEncoding : inputEncoding,
            outputEncoding: outputEncoding
        ]
        worker.process(Conversion.HTML, options, configuration)
    }
}
