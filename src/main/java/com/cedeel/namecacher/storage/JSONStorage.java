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

package com.cedeel.namecacher.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;

public class JSONStorage implements StorageBackend {
    private boolean isFunctional;
    private Path dataFile;
    private ObjectMapper objectMapper;
    private BiMap<UUID, String> players;

    public JSONStorage(Path outputFile) {
        try {
            dataFile = Files.createFile(outputFile);
            isFunctional = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        objectMapper = new ObjectMapper();
    }

    @Override
    public Map<UUID, String> getAll() {
        if (isFunctional)
            return players;
        return null;
    }

    @Override
    public void save(Map<UUID, String> players) {
        if (isFunctional) {
            players.putAll(players);
            persist();
        }

    }

    @Override
    public void add(UUID id, String name) {
        if (isFunctional) {
            players.put(id, name);
        }

    }

    @Override
    public void persist() {
        if (isFunctional) {
            try {
                objectMapper.writeValue(Files.newOutputStream(dataFile, StandardOpenOption.CREATE), players);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
