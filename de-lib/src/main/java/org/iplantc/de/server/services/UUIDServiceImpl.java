package org.iplantc.de.server.services;

import org.iplantc.de.client.services.UUIDService;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

/**
 * @author jstroot
 */
public class UUIDServiceImpl implements UUIDService {

    @Override
    public List<String> getUUIDs(int num) {
       List<String> uuids = Lists.newArrayList();
        for(int i = 0; i < num; i++){
            uuids.add(UUID.randomUUID().toString());
        }
        return uuids;
    }

}
