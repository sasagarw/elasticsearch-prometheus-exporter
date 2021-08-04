/*
 * Copyright [2016] [Vincent VAN HOLLEBEKE]
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
 *
 */

package org.elasticsearch.action;

import org.elasticsearch.ElasticsearchException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Utility class to help read files from plugin resources.
 * ATM we do not allow custom ES queries, and we provide some hardcoded queries instead.
 * To make the life easier we initialize and read the queries from file(s) found in resources.
 */
public abstract class FileIOUtils {

    /**
     * Return content of file having given name from root of the resources.
     *
     * @param resourceFileName Name of the file located relative to resources root
     * @return Content of the file
     */
    public static String readContentFromResources(String resourceFileName) {
        return FileIOUtils.readAndConcatFromInputStream(
                FileIOUtils.class.getResourceAsStream("/" + resourceFileName)
        );
    }

    /**
     * Read all lines from InputStream (assuming UTF-8) and concat them into a single String value.
     *
     * @param inputStream input stream to be converted to string
     * @return String representation of InputStream
     * @throws ElasticsearchException processing the input file failed
     */
    public static String readAndConcatFromInputStream(InputStream inputStream) throws ElasticsearchException {
        try {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line);
                }
            }
            return resultStringBuilder.toString();
        } catch (IOException exception) {
            throw new ElasticsearchException("Error reading inputStream {}", inputStream);
        }
    }
}
