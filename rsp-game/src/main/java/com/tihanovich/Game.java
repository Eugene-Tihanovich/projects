package com.tihanovich;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    public static void main(String[] args) {

        List<String> argsWithoutDuplicates = Arrays.stream(args)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());

        if (args.length >= 3 && args.length % 2 == 1 && args.length == argsWithoutDuplicates.size()) {

            byte[] playerKey = KeyUtil.getKey(16);
            int computerStep = new Random().nextInt(args.length);

            byte[] computerText = args[computerStep].getBytes(StandardCharsets.UTF_8);
            byte[] input = ByteBuffer.allocate(playerKey.length + computerText.length).
                    put(playerKey).
                    put(computerText).
                    array();
            System.out.println("HMAC: " + DigestUtils.sha3_256Hex(input).toUpperCase());

            System.out.println("Available moves:");
            for (int i = 0; i < args.length; i++) {
                System.out.println((i + 1) + " - " + args[i]);
            }
            System.out.println("0 - Exit");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your move: ");
            int playerStep = scanner.nextInt();

            if (playerStep == 0){
                System.out.println("Your move: Exit");
                System.out.println("Bye bye!");
            }
            else {
                while (playerStep > args.length || playerStep < 0) {
                    System.out.println("Wrong move! For example, enter 1");
                    System.out.println("Available moves:");

                    for (int i = 0; i < args.length; i++) {
                        System.out.println((i + 1) + " - " + args[i]);
                    }
                    System.out.println("0 - Exit");

                    System.out.print("Enter your move: ");
                    playerStep = scanner.nextInt();

                    if (playerStep == 0){
                        System.out.println("Your move: Exit");
                        System.exit(0);
                    }
                }

                System.out.println("Your move: " + args[playerStep - 1]);
                System.out.println("Computer move: " + args[computerStep]);
                String choeseComp = args[computerStep];

                for (int i = 0; i < playerStep; i++) {
                    String tmp = args[0];
                    for (int j = 0; j < args.length - 1; j++) {
                        args[j] = args[j + 1];
                    }
                    args[args.length - 1] = tmp;
                }

                int range = (args.length - 1) / 2;

                int counter = 0;
                for (int i = 0; i < range; i++) {
                    if (choeseComp.equals(args[i])) {
                        counter = counter + 1;
                    }
                }

                if (computerStep == (playerStep - 1)) {
                    System.out.println("Player and computer tie!");
                } else if (counter == 1) {
                    System.out.println("Computer wins!");
                } else {
                    System.out.println("Player wins!");
                }

                System.out.println("HMAC key: " + KeyUtil.bytesToHex(playerKey).toUpperCase());
            }
        } else {
            System.out.println("Oops! Something went wrong.\n" +
                    "You must enter an odd number of parameters from 3 or more. " +
                    "For example, Stone Scissors Paper");
        }
    }
}