package org.apache.spark.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.kafka.KafkaSparkContextManager
import scala.reflect.ClassTag
import kafka.message.MessageAndMetadata
import kafka.serializer.Decoder
import kafka.common.TopicAndPartition
import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
class SparkKafkaContext{
  var sc:SparkContext=null
  def this(sc:SparkContext){
    this()
    this.sc=sc
  }
  def this(conf:SparkConf){
    this()
    sc=new SparkContext(conf)
  }
  def sparkcontext()=sc
  def broadcast[T:ClassTag](value:T)={
    
    sc.broadcast(value)
  }
   //将当前的topic的groupid更新至最新的offsets
  def updataOffsetToLastest(topics:Set[String],kp: Map[String, String])={
    val lastestOffsets=KafkaSparkContextManager.getLatestOffsets(topics, kp)
    KafkaSparkContextManager.updateConsumerOffsets(kp, lastestOffsets)
    lastestOffsets
  }
  def updateRDDOffsets[T](kp: Map[String, String], groupId: String, rdd: RDD[T]){
    KafkaSparkContextManager.updateRDDOffset(kp, groupId, rdd)
  }
  def kafkaRDD[
    K: ClassTag,
    V: ClassTag,
    KD <: Decoder[K]: ClassTag,
    VD <: Decoder[V]: ClassTag,
    R: ClassTag](
      kp:Map[String, String],
      topics: Set[String],
      msgHandle: (MessageAndMetadata[K, V]) => R)={
    KafkaSparkContextManager.createKafkaRDD[K, V, KD, VD, R](sc, kp, topics, null, msgHandle)
  }
  def kafkaRDD[
    K: ClassTag,
    V: ClassTag,
    KD <: Decoder[K]: ClassTag,
    VD <: Decoder[V]: ClassTag,
    R: ClassTag](
      kp:Map[String, String],
      topics: Set[String],
      fromOffset: Map[TopicAndPartition, Long],
      msgHandle: (MessageAndMetadata[K, V]) => R)={
    KafkaSparkContextManager.createKafkaRDD[K, V, KD, VD, R](sc, kp, topics, fromOffset, msgHandle)
  }
  def kafkaRDD[R: ClassTag](
      kp:Map[String, String],
      topics: Set[String],
      msgHandle: (MessageAndMetadata[String, String]) => R)={
    KafkaSparkContextManager.createKafkaRDD[String, String, StringDecoder, StringDecoder, R](sc, kp, topics, null, msgHandle)
  }
  def kafkaRDD[R: ClassTag](
      kp:Map[String, String],
      topics: Set[String],
      fromOffset: Map[TopicAndPartition, Long],
      msgHandle: (MessageAndMetadata[String, String]) => R)={
    KafkaSparkContextManager.createKafkaRDD[String, String, StringDecoder, StringDecoder, R](sc, kp, topics, fromOffset, msgHandle)
  }
   def kafkaRDD[R: ClassTag](
      kp:Map[String, String],
      topics: Set[String],
      maxMessagesPerPartition:Int,
      msgHandle: (MessageAndMetadata[String, String]) => R)={
    KafkaSparkContextManager.createKafkaRDD[String, String, StringDecoder, StringDecoder, R](sc, kp, topics, null,maxMessagesPerPartition, msgHandle)
  } 
}