package com.es.test

import com.spark.es.util.ElasticsearchManagerTool

object Test {
   val address="192.168.10.115,192.168.10.110,192.168.10.81"
   val clusterName="zhiziyun"
  def main(args: Array[String]): Unit = {
    val client=ElasticsearchManagerTool.getESClient(address, clusterName)
  }
}