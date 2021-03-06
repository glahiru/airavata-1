/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/
package org.apache.airavata.gfac.core.monitor;

import com.google.common.eventbus.Subscribe;
import org.apache.airavata.common.utils.AiravataUtils;
import org.apache.airavata.common.utils.MonitorPublisher;
import org.apache.airavata.common.utils.ServerSettings;
import org.apache.airavata.common.utils.listener.AbstractActivityListener;
import org.apache.airavata.messaging.core.MessageContext;
import org.apache.airavata.messaging.core.Publisher;
import org.apache.airavata.model.messaging.event.*;
import org.apache.airavata.model.workspace.experiment.TaskDetails;
import org.apache.airavata.model.workspace.experiment.TaskState;
import org.apache.airavata.registry.cpi.Registry;
import org.apache.airavata.registry.cpi.RegistryModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class AiravataTaskStatusUpdator implements AbstractActivityListener {
    private final static Logger logger = LoggerFactory.getLogger(AiravataTaskStatusUpdator.class);
    private Registry airavataRegistry;
    private MonitorPublisher monitorPublisher;
    private Publisher publisher;
    
    public Registry getAiravataRegistry() {
        return airavataRegistry;
    }

    public void setAiravataRegistry(Registry airavataRegistry) {
        this.airavataRegistry = airavataRegistry;
    }

    @Subscribe
    public void setupTaskStatus(TaskStatusChangeRequestEvent taskStatus) throws Exception{
    	try {
			updateTaskStatus(taskStatus.getTaskIdentity().getTaskId(), taskStatus.getState());
			logger.debug("Publishing task status for "+taskStatus.getTaskIdentity().getTaskId()+":"+taskStatus.getState().toString());
            TaskStatusChangeEvent event = new TaskStatusChangeEvent(taskStatus.getState(), taskStatus.getTaskIdentity());
            monitorPublisher.publish(event);
            String messageId = AiravataUtils.getId("TASK");
            MessageContext msgCntxt = new MessageContext(event, MessageType.TASK, messageId, taskStatus.getTaskIdentity().getGatewayId());
            msgCntxt.setUpdatedTime(AiravataUtils.getCurrentTimestamp());
            if ( ServerSettings.isRabbitMqPublishEnabled()){
                publisher.publish(msgCntxt);
            }
		} catch (Exception e) {
            String msg = "Error persisting data task status to database...";
            logger.error(msg + e.getLocalizedMessage(), e);
            throw new Exception(msg, e);
		}
    }

    @Subscribe
    public void setupTaskStatus(JobStatusChangeEvent jobStatus) throws Exception{
    	TaskState state=TaskState.UNKNOWN;
    	switch(jobStatus.getState()){
    	case ACTIVE:
    		state=TaskState.EXECUTING; break;
    	case CANCELED:
    		state=TaskState.CANCELED; break;
    	case COMPLETE:
    		state=TaskState.POST_PROCESSING; break;
    	case FAILED:
    		state=TaskState.FAILED; break;
    	case HELD: case SUSPENDED: case QUEUED:
    		state=TaskState.WAITING; break;
    	case SETUP:
    		state=TaskState.PRE_PROCESSING; break;
    	case SUBMITTED:
    		state=TaskState.STARTED; break;
    	case UN_SUBMITTED:
    		state=TaskState.CANCELED; break;
    	case CANCELING:
    		state=TaskState.CANCELING; break;
		default:
			return;
    	}
    	try {
			updateTaskStatus(jobStatus.getJobIdentity().getTaskId(), state);
			logger.debug("Publishing task status for "+jobStatus.getJobIdentity().getTaskId()+":"+state.toString());
            TaskIdentifier taskIdentity = new TaskIdentifier(jobStatus.getJobIdentity().getTaskId(),
                                                         jobStatus.getJobIdentity().getWorkflowNodeId(),
                                                         jobStatus.getJobIdentity().getExperimentId(),
                                                         jobStatus.getJobIdentity().getGatewayId());
            TaskStatusChangeEvent event = new TaskStatusChangeEvent(state, taskIdentity);
            monitorPublisher.publish(event);
            String messageId = AiravataUtils.getId("TASK");
            MessageContext msgCntxt = new MessageContext(event, MessageType.TASK, messageId,jobStatus.getJobIdentity().getGatewayId());
            msgCntxt.setUpdatedTime(AiravataUtils.getCurrentTimestamp());
            if ( ServerSettings.isRabbitMqPublishEnabled()){
                publisher.publish(msgCntxt);
            }

        }  catch (Exception e) {
            logger.error("Error persisting data" + e.getLocalizedMessage(), e);
            throw new Exception("Error persisting task status..", e);
		}
    }
    
    public  TaskState updateTaskStatus(String taskId, TaskState state) throws Exception {
    	TaskDetails details = (TaskDetails)airavataRegistry.get(RegistryModelType.TASK_DETAIL, taskId);
        if(details == null) {
            logger.error("Task details cannot be null at this point");
            throw new Exception("Task details cannot be null at this point");
        }
        org.apache.airavata.model.workspace.experiment.TaskStatus status = new org.apache.airavata.model.workspace.experiment.TaskStatus();
        if(!TaskState.CANCELED.equals(details.getTaskStatus().getExecutionState())
                && !TaskState.CANCELING.equals(details.getTaskStatus().getExecutionState())){
            status.setExecutionState(state);
        }else{
            status.setExecutionState(details.getTaskStatus().getExecutionState());
        }
        status.setTimeOfStateChange(Calendar.getInstance().getTimeInMillis());
        details.setTaskStatus(status);
        logger.debug("Updating task status for "+taskId+":"+details.getTaskStatus().toString());

        airavataRegistry.update(RegistryModelType.TASK_STATUS, status, taskId);
        return status.getExecutionState();
    }

	public void setup(Object... configurations) {
		for (Object configuration : configurations) {
			if (configuration instanceof Registry){
				this.airavataRegistry=(Registry)configuration;
			} else if (configuration instanceof MonitorPublisher){
				this.monitorPublisher=(MonitorPublisher) configuration;
			} else if (configuration instanceof Publisher){
                this.publisher=(Publisher) configuration;
            }
        }
	}
}
