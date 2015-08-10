/*
 * Copyright 2015 Telefónica Investigación y Desarrollo, S.A.U
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
package es.tid.neosdp.plainmap;


/**
 * Exception when working with {@link PlainMap}).
 *
 * @author Jorge Lorenzo (jorgelg@tid.es)
 */
public class PlainMapException extends Exception {

    private static final long serialVersionUID = 934404218689912677L;

    /**
     * Constructor.
     *
     * @param message
     */
    public PlainMapException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message
     * @param t
     */
    public PlainMapException(final String message, final Throwable t) {
        super(message, t);
    }

}