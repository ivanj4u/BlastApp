package com.aribanilia.sms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

@Component
public class SmsUtil {

    @Value("${sms.dir}") private String dirSms;

    private final static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

    public void doSend(String noHandphone, String msg) {
        logger.info("Running Method doSend : " + noHandphone);

        /**
         * Setting CMD
         */
        StringBuffer cmd = new StringBuffer();
        // Change Dir to Installed Program
        cmd.append("cd /d " + dirSms);
        // Compose Sms
        cmd.append(" && ");
        cmd.append("smscaster.exe -Start");
        /** Send Sms */
        cmd.append(" && ");
        cmd.append("smscaster.exe -Compose " + noHandphone + " ");
        // put message
        cmd.append("\"").append(msg).append("\"");
        /** Stop
         cmd.append(" && ");
         cmd.append("smscaster.exe -Stop");
         */
        // init shell
        ProcessBuilder builder = new ProcessBuilder("cmd");
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            System.out.println(e);
        }
        // get stdin of shell
        BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

        // execute the desired command
        try {
            // single execution
            p_stdin.write(cmd.toString());
            p_stdin.newLine();
            p_stdin.flush();
        } catch (IOException e) {
            System.out.println(e);
        }

        // finally close the shell by execution exit command
        try {
            p_stdin.write("exit");
            p_stdin.newLine();
            p_stdin.flush();
        } catch (IOException e) {
            System.out.println(e);
        }

        // write stdout of shell (=output of all commands)
        Scanner s = new Scanner(p.getInputStream());
        while (s.hasNext()) {
            System.out.println(s.next());
        }
        s.close();

        logger.info("End Method doSend : " + noHandphone);
    }
}
