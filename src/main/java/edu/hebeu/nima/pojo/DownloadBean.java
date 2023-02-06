package edu.hebeu.nima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadBean {
    String storyname;
    List<Long> qqs;
}
