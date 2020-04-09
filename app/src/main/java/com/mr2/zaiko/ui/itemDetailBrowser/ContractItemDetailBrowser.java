package com.mr2.zaiko.ui.itemDetailBrowser;

import com.mr2.zaiko.domain.inhouse.equipment.EquipmentId;
import com.mr2.zaiko.domain.outside.commodity.CommodityId;
import com.mr2.zaiko.domain.outside.company.CompanyId;
import com.mr2.zaiko.domain.outside.product.ProductId;

public interface ContractItemDetailBrowser {
    interface View{
        void setResource(ItemDetailBrowserResource resource);

        // set views resource
        void setPrimaryName(String primaryName);
        void setMakerName(String name);
        void setModel(String model);
        void setProductName(String name);
        void setProductPrice(String productPrice);
        void setInventoryPrice(String inventoryPrice);
        void setKeyword(String[] array);
        void setSellerName(String name);
        void setPrimaryCommodityName(String name);
        void setPrimaryCommodityPrice(String primaryCommodityPrice);
        void setUnitSpinner(int[] spinnerItem);
        void setPrimaryCommodityUnit(String unit);

        void openImageCapture(); //カメラ起動
        void transitionToItemDetailEditor(ProductId productId); //編集
//        void hydeImageViewer();
        void transitionToListOfItemByKeyword(String keyword); //キーワード検索
        void transitionToListOfItemByMaker(CompanyId makerId); //メーカー検索
        void transitionToListOfCommodity(CommodityId commodityId); //商品詳細
        void transitionToListOfBackorder(EquipmentId equipmentId); //入荷待ち一覧
        void transitionToListOfExternalBarcode(EquipmentId equipmentId); //社外バーコード一覧
        void transitionToListOfStoringHistory(EquipmentId equipmentId); //入出庫履歴
        void transitionToListOfBuyHistory(EquipmentId equipmentId); //購入履歴
        void showDialog(String message); //Result、Cation等
        void transitionToInventoryList(EquipmentId equipmentId);
        void transitionToListOfSeller(ProductId productId);

    }

    interface Presenter{
        void onCreate(ProductId productId); // -> setResource(ItemDetailBrowserResource resource)

        void onClickPrimaryName();
        void onClickMakerName();
        void onClickInventoryMore();
        void onClickPutShoppingCart(int quantityWantToPutCart);
        void onClickKeyword(String keyword);
        void onCLickSellerName();
        void onClickCommodityMore();
        void onClickStoringMore();
        void onClickBuyHistoryMore();

        void onCreateView();
    }
}
