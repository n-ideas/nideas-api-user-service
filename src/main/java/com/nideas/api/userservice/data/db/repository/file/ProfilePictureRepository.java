package com.nideas.api.userservice.data.db.repository.file;

import com.nideas.api.userservice.data.db.entity.file.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Created by Nanugonda on 9/10/2018. */
@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {}
