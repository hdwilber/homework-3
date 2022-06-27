package com.inventory.taskrequest;

import java.util.EventListener;

public interface TaskRequestEventListener extends EventListener {
	public enum TaskRequestEventType {
		ADD,
		START,
		COMPLETE
	};

	public void onTaskRequestEvent(TaskExecutor source, TaskRequestEventType type, TaskRequest t);
}
