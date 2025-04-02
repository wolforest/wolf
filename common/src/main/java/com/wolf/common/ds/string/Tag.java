package com.wolf.common.ds.string;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * com.wolf.common.model.type.string
 *
 * @author Wingle
 * @since 2019/10/15 4:57 下午
 **/
public class Tag  {
    private String delimiter = ",";
    private final Set<String> tags;

    public Tag() {
        this(null, null);
    }

    public Tag(String tagString) {
        this(tagString, null);
    }

    public Tag(String tagString, String delimiter) {
        tags = new TreeSet<>();

        if (delimiter != null) {
            this.delimiter = delimiter;
        }
        addStringTag(tagString);
    }

    public Tag add(String tag) {
        if (tag == null) {
            return this;
        }

        tag = tag.trim();
        if (tags.contains(tag)) {
            return this;
        }

        tags.add(tag);
        return this;
    }

    public Tag addString(String tagString) {
        addStringTag(tagString);
        return this;
    }

    public Tag addAll(Collection<String> tagList) {
        for (String tag: tagList ) {
            add(tag);
        }
        return this;
    }

    public Tag remove(String tag) {
        if (tag == null) {
            return this;
        }

        tag = tag.trim();
        tags.remove(tag);

        return this;
    }

    public Tag removeString(String tagString) {
        removeStringTag(tagString);

        return this;
    }

    public Tag removeAll(Collection<String> tagList) {
        for (String tag: tagList ) {
            remove(tag);
        }
        return this;
    }

    public boolean contains(String tag) {
        if (tag == null) {
            return false;
        }

        return tags.contains(tag.trim());
    }

    @Override
    public String toString() {
        return String.join(delimiter, tags);
    }

    private void addStringTag(String tagString) {
        if (tagString == null) {
            return;
        }

        String[] tagArray = tagString.split(delimiter);

        for (String tag : tagArray ) {
            add(tag);
        }
    }

    private void removeStringTag(String tagString) {
        if (tagString == null) {
            return;
        }

        String[] tagArray = tagString.split(delimiter);

        for (String tag : tagArray ) {
            remove(tag);
        }
    }

}
