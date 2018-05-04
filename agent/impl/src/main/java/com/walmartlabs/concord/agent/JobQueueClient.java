package com.walmartlabs.concord.agent;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Wal-Mart Store, Inc.
 * -----
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
 * =====
 */

import com.walmartlabs.concord.project.InternalConstants;
import com.walmartlabs.concord.rpc.JobEntry;
import com.walmartlabs.concord.server.ApiException;
import com.walmartlabs.concord.server.ApiResponse;
import com.walmartlabs.concord.server.client.ProcessQueueApi;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JobQueueClient extends AbstractQueueClient<JobEntry> {

    private final ProcessQueueApi queueClient;

    public JobQueueClient(Configuration cfg) throws IOException {
        super(cfg);

        this.queueClient = new ProcessQueueApi(getClient());
    }

    protected JobEntry poll() throws ApiException {
        return withRetry(() -> {
            ApiResponse<File> resp = queueClient.takeWithHttpInfo();
            if (resp.getData() == null) {
                return null;
            }
            String instanceId = getHeader(InternalConstants.Headers.PROCESS_INSTANCE_ID, resp);
            return new JobEntry(UUID.fromString(instanceId), resp.getData().toPath());
        });
    }
}
