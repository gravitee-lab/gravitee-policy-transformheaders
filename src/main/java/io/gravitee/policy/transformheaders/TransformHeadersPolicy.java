/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.policy.transformheaders;

import io.gravitee.gateway.api.Request;
import io.gravitee.gateway.api.Response;
import io.gravitee.policy.api.PolicyChain;
import io.gravitee.policy.api.annotations.OnRequest;
import io.gravitee.policy.api.annotations.OnResponse;
import io.gravitee.policy.transformheaders.configuration.TransformHeadersPolicyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class TransformHeadersPolicy {

    /**
     * LOGGER
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(TransformHeadersPolicy.class);

    /**
     * Transform headers configuration
     */
    private final TransformHeadersPolicyConfiguration transformHeadersPolicyConfiguration;

    public TransformHeadersPolicy(final TransformHeadersPolicyConfiguration transformHeadersPolicyConfiguration) {
        this.transformHeadersPolicyConfiguration = transformHeadersPolicyConfiguration;
    }

    @OnRequest
    public void onRequest(Request request, Response response, PolicyChain policyChain) {
        // Remove request headers
        if (transformHeadersPolicyConfiguration.getRemoveHeaders() != null) {
            transformHeadersPolicyConfiguration.getRemoveHeaders()
                    .forEach(headerName -> request.headers().remove(headerName));
        }

        // Add or update request headers
        if (transformHeadersPolicyConfiguration.getAddHeaders() != null) {
            transformHeadersPolicyConfiguration.getAddHeaders().forEach(
                    header -> request.headers().add(header.getName(), header.getValue()));
        }

        // Apply next policy in chain
        policyChain.doNext(request, response);
    }

    @OnResponse
    public void onResponse(Request request, Response response, PolicyChain policyChain) {
        // Remove response headers
        if (transformHeadersPolicyConfiguration.getRemoveHeaders() != null) {
            transformHeadersPolicyConfiguration.getRemoveHeaders()
                    .forEach(headerName -> response.headers().remove(headerName));
        }

        // Add or update response headers
        if (transformHeadersPolicyConfiguration.getAddHeaders() != null) {
            transformHeadersPolicyConfiguration.getAddHeaders().forEach(
                    header -> request.headers().add(header.getName(), header.getValue()));
        }

        // Apply next policy in chain
        policyChain.doNext(request, response);
    }
}
