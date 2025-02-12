package cn.coderule.wolfno.wolfid;

import cn.coderule.common.lang.exception.SystemException;
import cn.coderule.wolfno.config.WolfNoConfig;
import cn.coderule.wolfno.model.WolfNoContext;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class WolfIDFetcher {
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static String SCHEMA_TABLE = "information_schema.tables ";
    private final static String UID_TABLE = "wolf_id";
    private final static int DEFAULT_ID = 11111111;
    private final static int DEFAULT_SHARD = 11;

    private final WolfNoConfig config;
    private final JdbcTemplate jdbcTemplate;

    public WolfIDFetcher(WolfNoConfig config, JdbcTemplate jdbcTemplate) {
        this.config = config;
        this.jdbcTemplate = jdbcTemplate;

        checkTableExists();
    }

    private void checkTableExists() {
        String sql = """
            SELECT table_name FROM %s
            WHERE table_schema = 'public'
            AND table_name = '%s'
        """.formatted(SCHEMA_TABLE, UID_TABLE);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

        if (result.isEmpty()) {
            throw new SystemException("Can not find wolfNo table with name " + UID_TABLE);
        }
    }

    public IDResult getID(WolfNoContext context) {
        String name =  getUidName(context);

        IDResult result = new IDResult();
        result.setStep(context.getStep());
        result.setShard(DEFAULT_SHARD);
        int initID = DEFAULT_ID + context.getStep();

        String sql = """
            INSERT INTO %s (name, shard, uid, step) values ('%s', %d, %d, %d)
            ON CONFLICT(name)
            DO UPDATE set
                uid = %s.uid + %d,
                updated_at = '%s'
            RETURNING uid
        """.formatted(UID_TABLE, name, DEFAULT_SHARD, initID, context.getStep(),
            UID_TABLE, context.getStep(), TIME_FORMATTER.format(context.getCreateTime()));

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class);
        if (null == id) {
            throw new SystemException("get wolfID failed");
        }

        result.setMaxID(id);
        return result;
    }

    private String getUidName(WolfNoContext context) {
        String name = context.getName();
        name += ":" + DAY_FORMATTER.format(context.getCreateTime());
        name += ":" + DEFAULT_SHARD;

        return name;
    }

}
