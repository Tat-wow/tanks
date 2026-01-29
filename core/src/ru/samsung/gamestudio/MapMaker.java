package ru.samsung.gamestudio;

import java.util.Random;

public class MapMaker {
    Random r;
    String up;
    String down;
    String middle;
    String map;

    public String makeMap() {
        r = new Random();

        // Генерация верхней части
        switch (r.nextInt(3)) {
            case 0: up = "###############\n#  #       #  #\n#  #       #  #"; break;
            case 1: up = "###############\n#    #####    #\n#    #####    #"; break;
            case 2: up = "###############\n##   #   #   ##\n##   #   #   ##"; break;
        }

        // Генерация средней части
        switch (r.nextInt(3)) {
            case 0: middle = "#             #\n#             #\n#  ##     ##  #\n#  ##     ##  #\n#  ##  #  ##  #\n#  ##  #  ##  #\n#  ##  #  ##  #\n#  ##     ##  #\n#             #"; break;
            case 1: middle = "#             #\n#             #\n#  #       #  #\n#  #  ###  #  #\n#  #   #   #  #\n#  #   #   #  #\n#  #   #   #  #\n#  #  ###  #  #\n#             #"; break;
            case 2: middle = "#             #\n#  #  ###  #  #\n#             #\n#             #\n#  #  ###  #  #\n#             #\n#             #\n#  #  ###  #  #\n#             #"; break;
        }

        // Генерация нижней части
        switch (r.nextInt(3)) {
            case 0: down = "###          ##\n#           ###\n#     ###   #o#\n###############"; break;
            case 1: down = "###   ###   ###\n#             #\n#    ##o##    #\n###############"; break;
            case 2: down = "##          ###\n###           #\n#o#   ###     #\n###############"; break;
        }

        // Собираем полную карту
        map = up + "\n" + middle + "\n" + down;
        return map;
    }

    public String getMap() {
        return map;
    }
}