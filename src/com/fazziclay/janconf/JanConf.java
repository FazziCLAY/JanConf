package com.fazziclay.janconf;

import java.util.*;

/**
 * <h1>JanConf</h1>
 *
 * <h2>Valid config</h2>
 * <pre>
 * # key comment
 * #key2 comment
 * #key3comment
 * key: value
 * # key json comment
 * key-json: [{"text":"json-value"}]
 * # group comment
 * #this group my life
 * group:
 *     # enabled comment
 *     enabled: false
 *     name:
 * # empty group comment
 * empty-group:
 * key-312312321:
 * 2dots: ::::::::::::
 * 2dots2: ::::::::::
 * 2dots3: : : : : 1 : : 2: sdff: : :1
 * commented-properties: true
 * double:
 * </pre>
 */
public class JanConf {
    public static final String VERSION_NAME = "1.4.4";
    public static final int VERSION_BUILD = 102;

    private final LinkedHashMap<String, CommentableObject> values = new LinkedHashMap<>();

    public JanConf() {}

    public JanConf(String source) {
        this(source.split("\n"));
    }

    private JanConf(String[] lines) {
        try {
            parse(lines);

        } catch (Exception e) {
            throw new JanException("Failed parse JanConf source.", e);
        }
    }

    /**
     * Remove value bo key
     */
    public JanConf remove(String key) {
        values.remove(key);
        return this;
    }

    /**
     * Get all keys
     */
    public Set<String> keys() {
        return values.keySet();
    }

    /**
     * Put comment to key. If key not exist: nothing
     */
    public JanConf putComment(String key, String comment) {
        final CommentableObject k = getCommentableObject(key);
        if (k != null) {
            k.comment = comment;
        }
        return this;
    }

    /**
     * Get key comment. Null if not exist
     */
    public String getComment(String key) {
        CommentableObject k = getCommentableObject(key);
        if (k != null) {
            return k.comment;
        }
        return null;
    }

    public boolean isCommented(String key) {
        return getComment(key) != null;
    }

    /**
     * Put value with comment
     */
    public JanConf put(String key, Object val, String comment) {
        if (key == null || val == null) throw new NullPointerException("key or val is null!");
        if (val instanceof JanConf) {
            values.put(key, new CommentableObject(val, comment));
        } else {
            values.put(key, new CommentableObject(val.toString(), comment));
        }
        return this;
    }

    /**
     * Put value without comment
     */
    public JanConf put(String key, Object val) {
        return put(key, val, null);
    }

    /**
     * Get value. If not exist: null
     */
    public String get(String key) {
        return get(key, null);
    }

    /**
     * Get value. If not exist: <code>def</code>
     * @param def value returned if key not exist
     */
    public String get(String key, Object def) {
        if (values.containsKey(key)) {
            return values.get(key).value.toString().replace("\n", "\\n");
        }
        return def.toString();
    }

    /**
     * Get int with {@link #getInt(String, int)} def: 0
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return Integer.parseInt(get(key, def));
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return Boolean.parseBoolean(get(key, def));
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long def) {
        return Long.parseLong(get(key, def));
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float def) {
        return Float.parseFloat(get(key, def));
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double def) {
        return Double.parseDouble(get(key, def));
    }

    public short getShort(String key) {
        return getShort(key, (short) 0);
    }

    public short getShort(String key, short def) {
        return Short.parseShort(get(key, def));
    }

    public JanConf getGroup(String key) {
        return getGroup(key, null);
    }

    public JanConf getGroup(String key, JanConf def) {
        if (values.containsKey(key)) {
            return (JanConf) values.get(key).value;
        }
        return def;
    }

    public JanConf getGroupSafe(String key) {
        if (values.containsKey(key)) {
            return (JanConf) values.get(key).value;
        }
        return new JanConf();
    }

    public boolean has(String key) {
        return values.containsKey(key);
    }

    /**
     * Get value type by key
     * @see ValueType
     * @return null if not exist. In other {@link ValueType}
     */
    public ValueType type(final String key) {
        if (!has(key)) return null;
        final CommentableObject obj = values.get(key);
        if (obj.value instanceof String) {
            return ValueType.STRING;
        } else if (obj.value instanceof JanConf) {
            return ValueType.GROUP;
        }
        throw new JanException("Found value type NOT String & NOT JanConf: " + obj.getClass());
    }

    @Override
    public String toString() {
        return toString(2, true);
    }

    public String toString(int indentSpaces) {
        return toString(2, true);
    }

    public String toString(int indentSpaces, boolean valueSpace) {
        try {
            return write(indentSpaces, valueSpace);
        } catch (Exception e) {
            throw new JanException("Failed export JanConf.", e);
        }
    }

    /**
     * 2 types GROUP and STRING
     */
    public enum ValueType {
        GROUP,
        STRING
    }

    private static class CommentableObject {
        private final Object value;
        private String comment;

        public CommentableObject(Object val, String comment) {
            this.value = val;
            this.comment = comment;
        }

        @Override
        public String toString() {
            return "CommentableObject{" +
                    "value=" + value +
                    ", comment='" + comment + '\'' +
                    '}';
        }
    }

    private String write(int indentSpaces, boolean valueSpace) {
        if (indentSpaces < 1) {
            throw new RuntimeException("Minimum 1 indentSpaces.");
        }
        StringBuilder s = new StringBuilder();

        String indentSpacesValue = multiplyChar(' ', indentSpaces);
        String firstValSpacesValue = valueSpace ? " " : "";

        values.forEach((key, val) -> {
            if (val.comment != null) {
                String[] comments = val.comment.split("\n");
                for (String comment : comments) {
                    s.append("# ").append(comment).append("\n");
                }
            }
            if (val.value instanceof String) {
                s.append(key).append(":").append(firstValSpacesValue).append(val.value);

            } else if (val.value instanceof JanConf) {
                String[] jan = ((JanConf)val.value).toString(indentSpaces, valueSpace).split("\n");
                s.append(key).append(":");
                for (String s1 : jan) {
                    s.append("\n").append(indentSpacesValue).append(s1);
                }
            }
            s.append("\n");
        });

        return s.toString().trim();
    }

    private String multiplyChar(char c, int count) {
        if (count == 0) return "";
        char[] fullSpacesArray = new char[count];
        Arrays.fill(fullSpacesArray, c);
        return new String(fullSpacesArray);
    }

    private void parse(String[] lines) {
        int foundIndent = -1;
        String key = null;
        List<String> groupTemp = new ArrayList<>();
        String comment = null;
        String groupComment = null;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (isSkipLine(line)) continue;
            if (isCommentLine(line)) {
                comment = getCommentLine(line, comment);
                continue;
            }

            int firstSpaces = countFirstSpaces(line);
            if (firstSpaces != 0) {
                if (foundIndent == -1) {
                    foundIndent = firstSpaces;
                    groupComment = comment;
                    comment = null;
                }
                groupTemp.add(line.substring(foundIndent));
                continue;
            } else {
                if (foundIndent != -1) {
                    foundIndent = -1;
                    put(key, new JanConf(groupTemp.toArray(new String[0])), groupComment);
                    groupTemp.clear();
                    groupComment = null;
                }
            }

            int splitPos = line.indexOf(":");
            key = line.substring(firstSpaces, splitPos);
            String value = line.substring(splitPos + 1).trim();


            boolean putEmpty = true;
            int li = i;
            while (li++ < lines.length-1) {
                String lline = lines[li];
                if (isSkipLine(lline) || isCommentLine(lline)) {
                    continue;
                }
                int s = countFirstSpaces(lline);
                if (s > 0) {
                    putEmpty = false;
                    groupComment = comment;
                } else {
                    break;
                }
            }

            if (putEmpty) {
                put(key, value, comment);
                comment = null;
            }
        }

        if (foundIndent != -1) {
            put(key, new JanConf(groupTemp.toArray(new String[0])), groupComment);
            groupTemp.clear();
        }
    }

    private boolean isSkipLine(String s) {
        return s.trim().isEmpty();
    }

    private boolean isCommentLine(String s) {
        return s.startsWith("#");
    }

    private String getCommentLine(String s, String oldLineComment) {
        if (!isCommentLine(s)) return null;
        String result;
        if (oldLineComment != null) {
            result = oldLineComment + "\n" + s.substring(1);
        } else {
            result = s.substring(1);
        }
        return result.trim();
    }


    private CommentableObject getCommentableObject(String key) {
        if (!values.containsKey(key)) return null;
        return values.get(key);
    }

    public static int countFirstSpaces(String s) {
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) != ' ') break;
            i++;
        }
        return i;
    }
}
