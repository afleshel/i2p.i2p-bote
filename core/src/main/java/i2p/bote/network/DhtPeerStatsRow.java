/**
 * Copyright (C) 2017  str4d@mail.i2p
 *
 * This file is part of I2P-Bote.
 * I2P-Bote is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * I2P-Bote is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with I2P-Bote.  If not, see <http://www.gnu.org/licenses/>.
 */

package i2p.bote.network;

import java.util.List;

/**
 * Holds information on a single peer's data, for displaying in the UI.
 */
public interface DhtPeerStatsRow {

    boolean isReachable();

    /**
     * Returns the data in each cell as a String.
     */
    List<String> toStrings();
}
