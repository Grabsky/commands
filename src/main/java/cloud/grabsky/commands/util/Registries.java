/*
 * MIT License
 *
 * Copyright (c) 2023 Grabsky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * HORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cloud.grabsky.commands.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class containing helper methods related to server registries.
 */
@Internal
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Registries {

    public static final Map<String, Material> MATERIAL = Collections.unmodifiableMap(toStaticMap(Registry.MATERIAL));
    public static final Map<String, Enchantment> ENCHANTMENT = Collections.unmodifiableMap(toStaticMap(Registry.ENCHANTMENT));
    public static final Map<String, EntityType> ENTITY_TYPE = Collections.unmodifiableMap(toStaticMap(Registry.ENTITY_TYPE));

    private static <T extends Keyed> Map<String, T> toStaticMap(final Registry<T> registry) {
        final Map<String, T> map = new HashMap<>();
        // ...
        registry.iterator().forEachRemaining(entry -> map.put(entry.key().asString(), entry));
        // ...
        return map;
    }

}
