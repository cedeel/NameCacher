// Imported from Mojang's AccountClient 2014-04-14

package com.cedeel.mojang.api.profiles;

public interface ProfileRepository {
    public Profile[] findProfilesByCriteria(ProfileCriteria... criteria);
}
