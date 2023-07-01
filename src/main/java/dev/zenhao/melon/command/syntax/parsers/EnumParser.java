package dev.zenhao.melon.command.syntax.parsers;

import dev.zenhao.melon.command.syntax.SyntaxChunk;
import dev.zenhao.melon.command.syntax.parsers.AbstractParser;
import java.util.ArrayList;
import java.util.Collections;

public class EnumParser
extends AbstractParser {
    String[] modes;

    public EnumParser(String ... modes) {
        this.modes = modes;
    }

    @Override
    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values2, String chunkValue) {
        if (chunkValue == null) {
            String s = "";
            for (String a : this.modes) {
                s = s + a + ":";
            }
            s = s.substring(0, s.length() - 1);
            return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + s + (thisChunk.isNecessary() ? ">" : "]");
        }
        ArrayList<String> possibilities = new ArrayList<String>();
        for (String s : this.modes) {
            if (!s.toLowerCase().startsWith(chunkValue.toLowerCase())) continue;
            possibilities.add(s);
        }
        if (possibilities.isEmpty()) {
            return "";
        }
        Collections.sort(possibilities);
        String s = (String)possibilities.get(0);
        return s.substring(chunkValue.length());
    }
}

