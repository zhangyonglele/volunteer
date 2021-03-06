package com.volunteer.volunteer.service;

import com.volunteer.volunteer.model.Picture;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PictureService {
    boolean insertPicture(Picture picture);

    List<Picture> getPictureByActivityId(int activityId);

}
