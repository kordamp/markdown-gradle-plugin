/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2013-2019 Andres Almiray.
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
package org.kordamp.gradle.plugin.markdown

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.kordamp.gradle.plugin.markdown.tasks.HtmlToMarkdownTask
import org.kordamp.gradle.plugin.markdown.tasks.MarkdownToHtmlTask

/**
 * @author Andres Almiray
 */
class MarkdownPlugin implements Plugin<Project> {
    private static final String DOCUMENTATION = 'Documentation'

    void apply(Project project) {
        Banner.display(project)

        project.tasks.register('markdownToHtml', MarkdownToHtmlTask,
            new Action<MarkdownToHtmlTask>() {
                @Override
                void execute(MarkdownToHtmlTask t) {
                    t.group = DOCUMENTATION
                    t.description = 'Converts Markdown sources into HTML'
                }
            })

        project.tasks.register('htmlToMarkdown', HtmlToMarkdownTask,
            new Action<HtmlToMarkdownTask>() {
                @Override
                void execute(HtmlToMarkdownTask t) {
                    t.group = DOCUMENTATION
                    t.description = 'Converts HTML into Markdown'
                }
            })
    }
}
