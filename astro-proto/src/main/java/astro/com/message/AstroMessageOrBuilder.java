// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MessageStructure.proto

package astro.com.message;

public interface AstroMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:AstroMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string topic = 1;</code>
   */
  java.lang.String getTopic();
  /**
   * <code>string topic = 1;</code>
   */
  com.google.protobuf.ByteString
      getTopicBytes();

  /**
   * <code>int32 index = 2;</code>
   */
  int getIndex();

  /**
   * <code>int64 datetime = 3;</code>
   */
  long getDatetime();

  /**
   * <code>string uuid = 4;</code>
   */
  java.lang.String getUuid();
  /**
   * <code>string uuid = 4;</code>
   */
  com.google.protobuf.ByteString
      getUuidBytes();

  /**
   * <code>string message = 5;</code>
   */
  java.lang.String getMessage();
  /**
   * <code>string message = 5;</code>
   */
  com.google.protobuf.ByteString
      getMessageBytes();
}
