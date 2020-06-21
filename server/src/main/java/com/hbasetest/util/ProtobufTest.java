package com.hbasetest.util;

import com.google.protobuf.ByteString;
import com.hbasetest.proto.HBaseNetProtocolModel;
import com.jihite.PersonModel;

public class ProtobufTest {
    public static void main(String[] args) throws Exception {
        HBaseNetProtocolModel.HBaseNetProtocol.Builder builder0 = HBaseNetProtocolModel.HBaseNetProtocol.newBuilder();
        builder0.setContent(ByteString.copyFromUtf8("test1"));
        HBaseNetProtocolModel.HBaseNetProtocol hBaseNetProtocol = builder0.build();
        hBaseNetProtocol.getContent().toByteArray();

        PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
        builder.setId(1);
        builder.setName("jihite");
        builder.setEmail("jihite@jihite.com");

        PersonModel.Person person = builder.build();
        System.out.println("before:" + person);

        System.out.println("===Person Byte:");
        for (byte b: person.toByteArray()) {
            System.out.println(b);
        }
        System.out.println("===================");

        byte[] byteArray = person.toByteArray();
        PersonModel.Person p2 = PersonModel.Person.parseFrom(byteArray);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());
    }
}
