/*
 * Copyright 2012-2014 the original author or authors.
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
package org.kordamp.gradle.markdown

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * @author Andres Almiray
 */
class MarkdownToHtmlTask extends DefaultTask {
    @InputDirectory File sourceDir
    @OutputDirectory File outputDir
    @Input Map configuration = [:]

    @Optional @Input String inputEncoding = 'UTF-8'
    @Optional @Input String outputEncoding = 'UTF-8'

    MarkdownWorker worker

    MarkdownToHtmlTask() {
        sourceDir = project.file('src/markdown')
        outputDir = new File(project.buildDir, 'gen-html')
        worker = new MarkdownWorkerImpl()
    }

    @TaskAction
    void runTask() {
        Map options = [
            sourceDir: sourceDir,
            outputDir: outputDir,
            inputEncoding: inputEncoding,
            outputEncoding: outputEncoding
        ]
        worker.process(Conversion.MARKDOWN, options, configuration)
    }
}
