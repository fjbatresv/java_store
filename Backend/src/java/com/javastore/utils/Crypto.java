/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static jdk.nashorn.tools.ShellFunctions.input;

/**
 *
 * @author 
 */
public class Crypto {

    public static String sha1(String password) throws NoSuchAlgorithmException {
        String hashed = null;
        //Transformador de texto
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        //Respuesta del transformador en bytes
        byte[] result = mDigest.digest(password.getBytes());
        //Constructor de string
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            //agregando al constructor de String
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        hashed = sb.toString();
        return hashed;
    }

}
