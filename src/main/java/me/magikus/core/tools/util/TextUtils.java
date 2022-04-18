package me.magikus.core.tools.util;

import me.magikus.core.entities.damage.DamageType;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.damage.modifiers.DamageModifier;
import me.magikus.core.stats.StatType;
import org.bukkit.ChatColor;

import java.text.CompactNumberFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TextUtils {
    public static List<String> wrap(String text, int width) {
        return wrap(text, width, ChatColor.GRAY);
    }

    public static String getDamageText(DamageModifier[] modifiers, Map<Element, Double> elementalInfo, boolean isCritical, DamageType type) {
        StringBuilder damageText = new StringBuilder();
        StringBuilder modifierText = new StringBuilder();
        if (isCritical) {
            modifierText.append(ChatColor.DARK_PURPLE).append("✧");
        }
        if (type == DamageType.PHYSICAL) {
            modifierText.append(ChatColor.RED).append("۞");
        } else {
            modifierText.append(ChatColor.LIGHT_PURPLE).append("☯");
        }
        boolean show = false;
        for (DamageModifier m : modifiers) {
            show = true;
            modifierText.append(m.color()).append(m.icon());
        }
        if (show) {
            damageText.append(ChatColor.GRAY).append("[").append(modifierText).append(ChatColor.GRAY).append("] ");
        }
        for (Element e : elementalInfo.keySet()) {
            if (elementalInfo.get(e) > 0) {
                damageText.append(e.color()).append(getHealthText(elementalInfo.get(e))).append(e.icon());
            }
        }
        return damageText.toString();
    }

    public static List<String> wrap(String text, int width, ChatColor defaultColor) {
        if (text == null) {
            return List.of(new String[]{""});
        } else if (text.length() <= width && !text.contains("\n")) {
            return List.of(new String[]{defaultColor + text});
        } else {
            char[] rawChars = (text + ' ').toCharArray();
            StringBuilder word = new StringBuilder();
            StringBuilder line = new StringBuilder();
            List<String> lines = new LinkedList<>();
            int lineColorChars = 0;

            int i;
            String partialWord;
            for (i = 0; i < rawChars.length; ++i) {
                char c = rawChars[i];
                if (c == 167) {
                    word.append(ChatColor.getByChar(rawChars[i + 1]));
                    lineColorChars += 2;
                    ++i;
                } else if (c != ' ' && c != '\n') {
                    word.append(c);
                } else {
                    int var10;
                    int var11;
                    String[] var12;
                    String[] split = word.toString().split("(?<=\\G.{" + width + "})");
                    if (line.length() == 0 && word.length() > width) {
                        var11 = (var12 = split).length;

                        for (var10 = 0; var10 < var11; ++var10) {
                            partialWord = var12[var10];
                            lines.add(partialWord);
                        }
                    } else if (line.length() + 1 + word.length() - lineColorChars == width) {
                        if (line.length() > 0) {
                            line.append(' ');
                        }

                        line.append(word);
                        lines.add(line.toString());
                        line = new StringBuilder();
                        lineColorChars = 0;
                    } else if (line.length() + 1 + word.length() - lineColorChars > width) {
                        var11 = (var12 = split).length;

                        for (var10 = 0; var10 < var11; ++var10) {
                            partialWord = var12[var10];
                            lines.add(defaultColor + line.toString());
                            line = new StringBuilder(partialWord);
                        }

                        lineColorChars = 0;
                    } else {
                        if (line.length() > 0) {
                            line.append(' ');
                        }

                        line.append(word);
                    }

                    word = new StringBuilder();
                    if (c == '\n') {
                        lines.add(defaultColor + line.toString());
                        line = new StringBuilder();
                    }
                }
            }

            if (line.length() > 0) {
                lines.add(line.toString());
            }

            for (i = 1; i < lines.size(); ++i) {
                String pLine = lines.get(i - 1);
                partialWord = lines.get(i);
                String color = ChatColor.getLastColors(pLine);
                lines.set(i, color + partialWord);
            }

            return List.of(lines.toArray(new String[0]));
        }
    }

    public static String capitalizeWord(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }

    public static String getHealthText(Double num) {
        final String[] cp
                = {"", "", "", "", "", "000K", "0M", "00M", "000M", "0B", "00B", "000B", "0T", "00T", "000T", "0Qa", "00Qa", "000Qa", "0Qi", "00Qi", "000Qi", "0Sx", "00Sx", "000Sx", "0Sp", "00Sp", "000Sp"};
        final DecimalFormat df
                = (DecimalFormat) NumberFormat.getInstance(new Locale("en", "US"));
        NumberFormat fmt = new CompactNumberFormat(df.toPattern(), df.getDecimalFormatSymbols(), cp);
        fmt.setMinimumFractionDigits(1);
        String format = fmt.format(num);
        if (format.contains(".0")) {
            format = format.replace(".0", "");
        }
        return format;
    }

    /**
     * Updates text, replacing stat names with their color and adding their
     * symbol in front. Also will highlight text green if surrounded with %%
     * and red if surrounded by @@.
     *
     * @param text The description / ability text to update
     * @return New text
     */
    public static String getHighlightedText(String text) {
        for (StatType t : StatType.values()) {
            if (t == StatType.DAMAGE) {
                continue;
            }
            text = text.replace(t.displayName(), t.color() + t.symbol() + " " + t.displayName() + ChatColor.GRAY);
        }
        boolean isGreen = false;
        boolean isRed = false;
        int count = 0;
        char[] chars = text.toCharArray();
        for (char c : chars) {
            if (count < chars.length - 1) {
                if (c == '%' && chars[count + 1] == '%') {
                    if (!isGreen) {
                        text = text.replaceFirst("%%", ChatColor.GREEN.toString());
                        isGreen = true;
                        isRed = false;
                    } else {
                        text = text.replaceFirst("%%", ChatColor.GRAY.toString());
                        isGreen = false;
                    }
                }
                if (c == '@' && chars[count + 1] == '@') {
                    if (!isRed) {
                        text = text.replaceFirst("@@", ChatColor.RED.toString());
                        isRed = true;
                        isGreen = false;
                    } else {
                        text = text.replaceFirst("@@", ChatColor.GRAY.toString());
                        isRed = false;
                    }
                }
            }
            count++;
        }
        return text;
    }

}
