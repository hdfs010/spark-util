package com.spark.test

import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.WatchedEvent

object ZookeeperTest {
   val connec="zk1,zk2,zk3"
  def main(args: Array[String]): Unit = {
    val zookeeper=new ZooKeeper(connec,100000,new WatcherD)
    println(zookeeper.getChildren("/stormoffset/test/smartadsclicklog", null))
    println( new String(zookeeper.getData("/stormoffset/test/smartadsclicklog/partition_0", false, null)))
    
  }
  class WatcherD extends Watcher{
   override def process(event:WatchedEvent) {
        System.out.println("收到事件通知：" + event.getState + "\n");
        /*if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }*/
    }

  }
}