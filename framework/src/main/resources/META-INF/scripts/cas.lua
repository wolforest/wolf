-- compare and set
local currValue = redis.call('GET', KEYS[1]);
local ttl = tonumber(ARGV[3])

-- auto renew if key not expired
if currValue ~= false and ttl > 0 then
    redis.call('EXPIRE', KEYS[1], ttl);
end

-- if key not exists, cas passes
if currValue == false or currValue == ARGV[1] then
    local r = redis.call('SET', KEYS[1], ARGV[2]);

    if ttl > 0 then
        redis.call('EXPIRE', KEYS[1], ttl);
    end

    return r
else
    return 0
end