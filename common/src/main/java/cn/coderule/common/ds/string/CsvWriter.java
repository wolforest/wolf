package cn.coderule.common.ds.string;

import lombok.extern.slf4j.Slf4j;
import cn.coderule.common.util.lang.collection.CollectionUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * com.wolf.common.io.file
 *
 * @author Wingle
 * @since 2021/10/31 下午8:53
 **/
@Slf4j
public class CsvWriter {
    private final String fileName;
    private final boolean override;
    private final boolean escape;
    private final Collection<Collection<String>> buffer;

    private FileWriter fileWriter;

    public CsvWriter(String fileName) {
        this(fileName, false);
    }

    public CsvWriter(String fileName, boolean override) {
        this(fileName, override, false);
    }

    public CsvWriter(String fileName, boolean override, boolean escape) {
        this.fileName = fileName;
        this.override = override;
        this.escape = escape;
        buffer = new ArrayList<>();
    }


    public void addRow(Collection<String> row) {
        if (CollectionUtil.isEmpty(row)) {
            return;
        }

        buffer.add(row);
    }

    public void addRows(Collection<Collection<String>> rows) {
        if (CollectionUtil.isEmpty(rows)) {
            return;
        }

        buffer.addAll(rows);
    }

    public int size() {
        return buffer.size();
    }

    public void flush() {
        if (CollectionUtil.isEmpty(buffer)) {
            return;
        }

        try {
            init();
            fileWriter.write(formatBuffer());
            fileWriter.flush();
            buffer.clear();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new cn.coderule.common.lang.exception.lang.IOException(fileName);
        }
    }

    public void close() {
        buffer.clear();
        if (null == fileWriter) {
            return;
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new cn.coderule.common.lang.exception.lang.IOException(fileName);
        }
    }

    public String escapeSpecialCharacters(String data) {
        if (!escape) {
            return data;
        }

        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private String formatBuffer() {
        StringBuilder sb = new StringBuilder();
        for (Collection<String> row : buffer) {
            sb.append(formatRow(row));
            sb.append("\n");
        }

        return sb.toString();
    }

    private String formatRow(Collection<String> row) {
        if (CollectionUtil.isEmpty(row)) {
            return "";
        }

        return row.stream()
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private void init() throws IOException {
        if (fileWriter != null) {
            return;
        }

        File file = new File(fileName);
        if (file.exists() && override) {
            fileWriter = new FileWriter(fileName);
            clearFile();
            return;
        }

        fileWriter = new FileWriter(fileName, true);
    }

    private void clearFile() {
        try {
            fileWriter.write("");
            fileWriter.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new cn.coderule.common.lang.exception.lang.IOException(fileName);
        }

    }
}
