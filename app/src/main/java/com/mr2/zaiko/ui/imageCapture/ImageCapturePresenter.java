package com.mr2.zaiko.ui.imageCapture;

import androidx.annotation.NonNull;

import com.mr2.zaiko.domain.inhouse.equipment.EquipmentId;
import com.mr2.zaiko.useCase.ImagePersistenceService;

public class ImageCapturePresenter implements ContractImageCapture.Presenter {
    private ContractImageCapture.View view;
    private ImagePersistenceService service;
    private EquipmentId targetEquipmentId;

    public ImageCapturePresenter(ImagePersistenceService service, EquipmentId targetEquipmentId) {
        this.service = service;
        this.targetEquipmentId = targetEquipmentId;
    }

    @Override
    public void onCreate(ContractImageCapture.View view) {
        this.view = view;
    }

    @Override
    public void onDestroy(@NonNull ContractImageCapture.View view) {
        this.view = null;
    }

    @Override
    public void onCaptureResult(String fileName) {
        service.addPhoto(targetEquipmentId, fileName);
    }
}
