if redis.call('hsetnx', KEYS[1], 'count', 1) == 1
then
    redis.call('expire', KEYS[1], ARGV[1]);
    return 1;
else
    local count = tonumber(redis.call('hget', KEYS[1], 'count'));
    redis.call('hmset', KEYS[1], 'count', count + 1);
    return count + 1;
end