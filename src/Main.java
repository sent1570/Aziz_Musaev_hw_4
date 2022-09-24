import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static int bossHealth = 1000;
    public static int bossDamage = 50;
    public static String bossDefenceType;
    public static int[] heroesHealth = {250, 180, 270, 500, 220, 230, 240, 210};
    public static int[] heroesDamage = {25, 20, 15, 5, 25, 20, 15};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Golem", "Lucky", "Berserk", "Thor"};
    public static int roundNumber = 0;
    static Random random = new Random();
    static int randomLucky;
    static int randomBerserk;
    static int blockedDamage;
    static int randomThor;

    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossHits();
        heroesHit();
        Golemdefens();
        recovery();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefenceType = heroesAttackType[randomIndex];
    }

    public static void bossHits() {
        randomLucky = random.nextInt(2);
        randomBerserk = random.nextInt(5) + 2;
        randomThor = random.nextInt(2);
        blockedDamage = bossDamage / randomBerserk;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (randomThor == 0 || heroesHealth[6] < 0) {
                if (heroesHealth[i] > 0) {
                     if(heroesHealth[5] - (bossDamage - blockedDamage) < 0 && i == 5 && heroesHealth[3] <= 0){
                         heroesHealth[i] = 0;
                     }
                    else if (heroesHealth[5] - ((bossDamage - (bossDamage / 5)) - blockedDamage)< 0 && i == 5 && heroesHealth[3] > 0){
                        heroesHealth[i] = 0;
                     }
                    else if(heroesHealth[i] - bossDamage + (bossDamage / 5) < 0 && heroesHealth[3] > 0){
                        heroesHealth[i] = 0;
                    }
                    else if (heroesHealth[i] - bossDamage < 0 && heroesHealth[3] <= 0) {
                        heroesHealth[i] = 0;
                    } else if (randomLucky == 1 && i == 4) {
                        heroesHealth[4] = heroesHealth[4];
                        System.out.println("Lucky is ability activated");
                    } else {
                        if (i == 5 && heroesHealth[3] > 0) {
                            heroesHealth[5] = heroesHealth[5] - ((bossDamage - (bossDamage / 5)) - blockedDamage);
                            System.out.println("Berserker damage blocked: " + blockedDamage);
                        }
                        else if(i == 5 && heroesHealth[3] <= 0){
                            heroesHealth[5] = heroesHealth[5] - (bossDamage - blockedDamage);
                            System.out.println("Berserker damage blocked: " + blockedDamage);
                        }
                        else if (i == 3) {
                            heroesHealth[3] = (heroesHealth[3] - bossDamage) - (bossDamage / 5 * (heroesDamage.length));
                            if (heroesHealth[3] < 0) {
                                heroesHealth[3] = 0;
                            }
                        } else if (heroesHealth[3] > 0){
                            heroesHealth[i] = heroesHealth[i] - bossDamage + (bossDamage / 5);
                        }
                        else{
                            heroesHealth[i] = heroesHealth[i] - bossDamage;
                        }
                    }
                }
            }
        }
        if (randomThor == 1) {
            System.out.println("Thos is ability activated");
        }
    }

    public static void heroesHit() {

        for (int i = 0; i < heroesAttackType.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                if (i == 5) {
                    heroesDamage[5] = heroesDamage[5] + blockedDamage;
                }
                int hit = heroesDamage[i];
                if (bossDefenceType == heroesAttackType[i]) {
                    Random random = new Random();
                    int coeff = random.nextInt(7) + 2; // 2,3,4,5,6,7,8
                    hit = coeff * heroesDamage[i];
                    System.out.println("Critical Damage: " + hit);
                }
                if (bossHealth - hit < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - hit; // bossHealth -= heroesDamage[i]
                }
            }
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }

        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void recovery() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (i == 7) {
                continue;
            }
            if (heroesHealth[7] > 0 && heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                int heal = random.nextInt(100 + 10);
                heroesHealth[i] = heroesHealth[i] + heal;
                System.out.println("Medic help: " + heal + " to " + heroesAttackType[i]);
                break;

            }
        }

    }

    public static void Golemdefens() {
        if (randomThor == 0 || heroesHealth[6] < 0 && heroesHealth[3] > 0) {
            int damageAbsorbed = bossDamage / 5 * (heroesDamage.length);
            System.out.println("Damage absorbed by the golem: " + damageAbsorbed);
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " --------------");
        System.out.println("Boss health: " + bossHealth + "; damage: "
                + bossDamage + "; defence: " + (bossDefenceType == null ? "No defence" : bossDefenceType));
        for (int i = 0; i < heroesHealth.length; i++) {
            if (i == 7) {
                System.out.println("Medic health: " + heroesHealth[7]);
            } else {
                System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + "; damage: "
                        + heroesDamage[i]);
            }
        }

    }
}
