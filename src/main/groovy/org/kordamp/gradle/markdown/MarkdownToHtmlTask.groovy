/*
 * Copyright 2013-2015 the original author or authors.
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

import java.nio.charset.StandardCharsets

/**
 * @author Andres Almiray
 */
class MarkdownToHtmlTask extends DefaultTask {
    @InputDirectory File sourceDir
    @OutputDirectory File outputDir
    @Input Map configuration = [:]

    @Optional @Input String inputEncoding = StandardCharsets.UTF_8.displayName()
    @Optional @Input String outputEncoding = StandardCharsets.UTF_8.displayName()

    @Optional @Input Boolean hardwraps
    @Optional @Input Boolean autoLinks
    @Optional @Input Boolean abbreviations
    @Optional @Input Boolean definitionLists
    @Optional @Input Boolean smartQuotes
    @Optional @Input Boolean smartPunctuation
    @Optional @Input Boolean smart
    @Optional @Input Boolean fencedCodeBlocks
    @Optional @Input Boolean tables
    @Optional @Input Boolean all
    @Optional @Input Boolean removeHtml
    @Optional @Input Boolean removeTables
    @Optional @Input String baseUri
    @Optional @Input Closure customizePegdown
    @Optional @Input Closure customizeRemark

    MarkdownWorker worker

    MarkdownToHtmlTask() {
        sourceDir = project.file('src/markdown')
        outputDir = new File(project.buildDir, 'gen-html')
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
        worker.process(Conversion.MARKDOWN, options, mergeConfiguration())
    }

    @SuppressWarnings('UnnecessaryObjectReferences')
    private Map mergeConfiguration() {
        Map config = [:]

        config.putAll(configuration)
        if (hardwraps != null) config.hardwraps = hardwraps
        if (autoLinks != null) config.autoLinks = autoLinks
        if (abbreviations != null) config.abbreviations = abbreviations
        if (definitionLists != null) config.definitionLists = definitionLists
        if (smartQuotes != null) config.smartQuotes = smartQuotes
        if (smartPunctuation != null) config.smartPunctuation = smartPunctuation
        if (smart != null) {
            config.smartQuotes = smart
            config.smartPunctuation = smart
        }
        if (fencedCodeBlocks != null) config.fencedCodeBlocks = fencedCodeBlocks
        if (tables != null) config.tables = tables
        if (all != null) {
            config.hardwraps = all
            config.autoLinks = all
            config.abbreviations = all
            config.definitionLists = all
            config.smartQuotes = all
            config.smartPunctuation = all
            config.fencedCodeBlocks = all
            config.tables = all
        }
        if (removeHtml != null) config.removeHtml = removeHtml
        if (removeTables != null) config.removeTables = removeTables
        if (baseUri != null) config.baseUri = baseUri
        if (customizePegdown != null) config.customizePegdown = customizePegdown
        if (customizeRemark != null) config.customizeRemark = customizeRemark

        config
    }
}
