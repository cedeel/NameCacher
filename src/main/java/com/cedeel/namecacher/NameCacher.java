/*
 * Copyright (c) 2014, Chris Darnell
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cedeel.namecacher;

import com.cedeel.mojang.api.profiles.HttpProfileRepository;
import com.cedeel.mojang.api.profiles.Profile;
import com.cedeel.mojang.api.profiles.ProfileCriteria;
import com.cedeel.namecacher.storage.StorageBackend;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.UUID;

public class NameCacher {
    private static BiMap<UUID, String> players;
    private StorageBackend storageBackend;

    public NameCacher(StorageBackend backend) {
        storageBackend = backend;
        players = HashBiMap.create(storageBackend.getAll());
    }

    public void addUser(UUID userId, String username) {
        if (players.put(userId, username) == null)
            storageBackend.add(userId, username);
    }

    public static String getName(final UUID id) {
        return NameCacher.players.get(id);
    }

    public static UUID getUUID(final String name) {
        UUID result = NameCacher.players.inverse().get(name);
        if (result == null) {
            result = getIdFromMojang(name);
        }
        return result;
    }

    private static UUID getIdFromMojang(final String name) {
        HttpProfileRepository repo = new HttpProfileRepository();
        Profile[] result = repo.findProfilesByCriteria(new ProfileCriteria(name, "minecraft"));
        if (result.length > 0) {
            return UUID.fromString(result[0].getId());
        } else return null;
    }
}
