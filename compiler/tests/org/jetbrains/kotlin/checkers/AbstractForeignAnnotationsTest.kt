/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.checkers

import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.test.MockLibraryUtil
import java.io.File

val FOREIGN_ANNOTATIONS_SOURCES_PATH = "compiler/testData/foreignAnnotations/annotations"

abstract class AbstractForeignAnnotationsTest : AbstractDiagnosticsWithFullJdkTest() {
    override fun getExtraClasspath(): List<File> =
            listOf(MockLibraryUtil.compileJvmLibraryToJar(annotationsPath, "foreign-annotations"))

    open protected val annotationsPath: String
        get() = FOREIGN_ANNOTATIONS_SOURCES_PATH

    override fun loadLanguageVersionSettings(module: List<TestFile>): LanguageVersionSettings {
        return LanguageVersionSettingsImpl(LanguageVersion.LATEST_STABLE, ApiVersion.LATEST_STABLE).apply {
            switchFlag(AnalysisFlags.loadJsr305Annotations, true)
        }
    }
}
