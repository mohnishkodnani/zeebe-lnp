Zeebe-java is the one worker for the bpmn  of type mkodnanitask1. This is a spring-zeebe worker using 0.26.2 client internally. 

The workflow is lnp.bpmn. Its just 1 task between start and end. 

The k8s folder contains what was deployed on K8s with all the settings. 
These are mostly default, except few changes. 

* data emptyDir with in-memory disk is mounted to simulate RAM Disk behavior, since we do not care of state and want minimum latency. 
* replication is set to 1. 
* there is only 1 broker with version 1.0.0-alpha2. 
* No Elastic search exporter is set. 
* Created a headless service for Gateway to access it from outside the K8s, so we can run lnp from local machines.

The problem is in a couple of runs we get DEADLINE_EXCEEDED between gateway and broker after 10000milliseconds.


LOG Errors On Brokers filled with the following warning:
```2021-03-16 18:32:48.019 [Broker-0-SnapshotDirector-1] [Broker-0-zb-fs-workers-0] INFO
      io.zeebe.logstreams.snapshot - Finished taking snapshot, need to wait until last written event position 38145 is committed, current commit position is 38145. After that snapshot can be marked as valid.
2021-03-16 18:32:48.021 [Broker-0-SnapshotDirector-1] [Broker-0-zb-fs-workers-1] INFO
      io.zeebe.logstreams.snapshot - Current commit position 38145 >= 38145, snapshot 33731-1-1615919568007-38143-9223372036854775807 is valid and has been persisted.
2021-03-16 18:32:48.021 [Broker-0-SnapshotStore-1] [Broker-0-zb-actors-1] WARN
      io.zeebe.snapshots.broker.impl.FileBasedTransientSnapshot - Failed to delete pending snapshot FileBasedTransientSnapshot{directory=/usr/local/zeebe/data/raft-partition/partitions/1/pending/33548-1-1615919268008-37927-9223372036854775807, snapshotStore=io.zeebe.snapshots.broker.impl.FileBasedSnapshotStore@5790bc60, metadata=FileBasedSnapshotMetadata{index=33548, term=1, timestamp=2021-03-16 06:27:48,008, processedPosition=37927, exporterPosition=9223372036854775807}}
java.nio.file.NoSuchFileException: /usr/local/zeebe/data/raft-partition/partitions/1/pending/33548-1-1615919268008-37927-9223372036854775807
	at sun.nio.fs.UnixException.translateToIOException(Unknown Source) ~[?:?]
	at sun.nio.fs.UnixException.rethrowAsIOException(Unknown Source) ~[?:?]
	at sun.nio.fs.UnixException.rethrowAsIOException(Unknown Source) ~[?:?]
	at sun.nio.fs.UnixFileAttributeViews$Basic.readAttributes(Unknown Source) ~[?:?]
	at sun.nio.fs.UnixFileSystemProvider.readAttributes(Unknown Source) ~[?:?]
	at sun.nio.fs.LinuxFileSystemProvider.readAttributes(Unknown Source) ~[?:?]
	at java.nio.file.Files.readAttributes(Unknown Source) ~[?:?]
	at java.nio.file.FileTreeWalker.getAttributes(Unknown Source) ~[?:?]
	at java.nio.file.FileTreeWalker.visit(Unknown Source) ~[?:?]
	at java.nio.file.FileTreeWalker.walk(Unknown Source) ~[?:?]
	at java.nio.file.Files.walkFileTree(Unknown Source) ~[?:?]
	at java.nio.file.Files.walkFileTree(Unknown Source) ~[?:?]
	at io.zeebe.util.FileUtil.deleteFolder(FileUtil.java:47) ~[zeebe-util-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.snapshots.broker.impl.FileBasedTransientSnapshot.abortInternal(FileBasedTransientSnapshot.java:145) ~[zeebe-snapshots-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.snapshots.broker.impl.FileBasedTransientSnapshot.lambda$abort$2(FileBasedTransientSnapshot.java:96) ~[zeebe-snapshots-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.util.sched.ActorJob.invoke(ActorJob.java:73) [zeebe-util-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.util.sched.ActorJob.execute(ActorJob.java:39) [zeebe-util-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.util.sched.ActorTask.execute(ActorTask.java:122) [zeebe-util-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.util.sched.ActorThread.executeCurrentTask(ActorThread.java:94) [zeebe-util-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.util.sched.ActorThread.doWork(ActorThread.java:78) [zeebe-util-1.0.0-alpha2.jar:1.0.0-alpha2]
	at io.zeebe.util.sched.ActorThread.run(ActorThread.java:191) [zeebe-util-1.0.0-alpha2.jar:1.0.0-alpha2]```

full logs in broker.log

LOGS on GATEWAY

Located in file gateway.log

LOGS for task1

located in file task.log


