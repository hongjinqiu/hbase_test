package com.hbasetest.util;

import com.jihite.PersonModel;

public class ProtobufTest {
    public static void main(String[] args) throws Exception {
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
