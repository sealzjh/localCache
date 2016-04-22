import com.dada.taskpool.service.lib.localcache.LocalCache;
import com.dada.taskpool.web.core.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class ClientController {

    @Resource(name = "localCache")
    private LocalCache localCache;

    @RequestMapping(value = "/getStrData", method = RequestMethod.GET)
    @ResponseBody
    public Response getStrData(@PathVariable("cityCode") String cityCode) {
        return Response.success(localCache.getStrData());
    }

    @RequestMapping(value = "/getHashData", method = RequestMethod.GET)
    @ResponseBody
    public Response getHashData(@PathVariable("cityCode") String cityCode) {
        return Response.success(localCache.getHashData());
    }

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    @ResponseBody
    public Response set(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key,
            @RequestParam(value = "value", required = true) String value,
            @RequestParam(value = "timeout", required = false, defaultValue = "0") Integer timeout
            ) {
        if(timeout > 0) {
            localCache.set(key, value, timeout);
        } else {
            localCache.set(key, value);
        }
        return Response.success("ok");
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Response get(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key
            ) {
        return Response.success(localCache.get(key));
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    @ResponseBody
    public Response del(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key
            ) {
        localCache.del(key);
        return Response.success("ok");
    }

    @RequestMapping(value = "/hset", method = RequestMethod.GET)
    @ResponseBody
    public Response hset(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key,
            @RequestParam(value = "fieldkey", required = true) String fieldKey,
            @RequestParam(value = "value", required = true) String value,
            @RequestParam(value = "timeout", required = false, defaultValue = "0") Integer timeout
    ) {
        if(timeout > 0) {
            localCache.hset(key, fieldKey, value, timeout);
        } else {
            localCache.hset(key, fieldKey, value);
        }
        return Response.success("ok");
    }

    @RequestMapping(value = "/hgetall", method = RequestMethod.GET)
    @ResponseBody
    public Response hgetAll(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key
            ) {
        return Response.success(localCache.hgetAll(key));
    }

    @RequestMapping(value = "/hget", method = RequestMethod.GET)
    @ResponseBody
    public Response hget(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key,
            @RequestParam(value = "fieldkey", required = true) String fieldKey
            ) {
        return Response.success(localCache.hget(key, fieldKey));
    }

    @RequestMapping(value = "/hdelAll", method = RequestMethod.GET)
    @ResponseBody
    public Response hdelAll(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key
            ) {
        localCache.hdelAll(key);
        return Response.success("ok");
    }

    @RequestMapping(value = "/hdel", method = RequestMethod.GET)
    @ResponseBody
    public Response hdel(
            @PathVariable("cityCode") String cityCode,
            @RequestParam(value = "key", required = true) String key,
            @RequestParam(value = "fieldkey", required = true) String fieldKey
            ) {
        localCache.hdel(key, fieldKey);
        return Response.success("ok");
    }

}
