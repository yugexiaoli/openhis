package com.twofish.service;

import com.twofish.domain.Medicines;
import com.twofish.dto.MedicinesDto;
import com.twofish.vo.DataGridView;

public interface MedicinesService {

    /**
     * 分页查询所有药品信息表
     * @param medicinesDto
     * @return
     */
    DataGridView listMedicinesForPage(MedicinesDto medicinesDto);

    /**
     * 添加药品信息表
     * @param medicinesDto
     * @return
     */
    int addMedicines(MedicinesDto medicinesDto);

    /**
     * 根据id查询药品信息表
     * @param stockMedicinesId
     * @return
     */
    Medicines getMedicinesById(Long stockMedicinesId);

    /**
     * 修改药品信息表
     * @param medicinesDto
     * @return
     */
    int updateMedicines(MedicinesDto medicinesDto);

    /**
     * 删除药品信息表(可批量)
     * @param medicinesIds
     * @return
     */
    int deleteMedicinesByIds(Long[] medicinesIds);

    /**
     * 查询所有可用的药品信息表
     * @return
     */
    DataGridView selectAllMedicines();

    /**
     * 调整药品库存
     * @param medicinesId
     * @param medicinesStockNum
     * @return
     */
    int updateMedicinesStorage(Long medicinesId, Long medicinesStockNum);
}
