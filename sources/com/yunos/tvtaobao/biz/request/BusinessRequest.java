package com.yunos.tvtaobao.biz.request;

import android.content.Context;
import android.text.TextUtils;
import anetwork.channel.Network;
import anetwork.channel.Response;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import anetwork.channel.statist.StatisticData;
import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.base.Unit;
import com.taobao.wireless.detail.DetailConfig;
import com.taobao.wireless.detail.api.DetailApiProxy;
import com.taobao.wireless.detail.api.DetailApiRequestor;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.RunMode;
import com.yunos.tv.core.disaster_tolerance.DisasterTolerance;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.AddBagBo;
import com.yunos.tvtaobao.biz.request.bo.AddFollowResult;
import com.yunos.tvtaobao.biz.request.bo.Address;
import com.yunos.tvtaobao.biz.request.bo.BonusBean;
import com.yunos.tvtaobao.biz.request.bo.BuildOrderRebateBo;
import com.yunos.tvtaobao.biz.request.bo.BuildOrderRequestBo;
import com.yunos.tvtaobao.biz.request.bo.CartStyleBean;
import com.yunos.tvtaobao.biz.request.bo.Cat;
import com.yunos.tvtaobao.biz.request.bo.CollectList;
import com.yunos.tvtaobao.biz.request.bo.CollectionsInfo;
import com.yunos.tvtaobao.biz.request.bo.CouponList;
import com.yunos.tvtaobao.biz.request.bo.CouponReceiveParamsBean;
import com.yunos.tvtaobao.biz.request.bo.CouponRecommendList;
import com.yunos.tvtaobao.biz.request.bo.CreateOrderResult;
import com.yunos.tvtaobao.biz.request.bo.DoPayOrders;
import com.yunos.tvtaobao.biz.request.bo.DynamicRecommend;
import com.yunos.tvtaobao.biz.request.bo.ElemBindBo;
import com.yunos.tvtaobao.biz.request.bo.ElemeOauthBo;
import com.yunos.tvtaobao.biz.request.bo.ElemeRecommandBo;
import com.yunos.tvtaobao.biz.request.bo.FeiZhuBean;
import com.yunos.tvtaobao.biz.request.bo.FindSameContainerBean;
import com.yunos.tvtaobao.biz.request.bo.FollowData;
import com.yunos.tvtaobao.biz.request.bo.GetFootMarkResponse;
import com.yunos.tvtaobao.biz.request.bo.GetFootMarkSummationResponse;
import com.yunos.tvtaobao.biz.request.bo.GoodsList;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchMO;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchResult;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchResultDo;
import com.yunos.tvtaobao.biz.request.bo.GoodsSearchZtcResult;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsBean;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsData;
import com.yunos.tvtaobao.biz.request.bo.JhsItemDetail;
import com.yunos.tvtaobao.biz.request.bo.JhsNormalGoodsBean;
import com.yunos.tvtaobao.biz.request.bo.JhsSuperChoiceGoodsBean;
import com.yunos.tvtaobao.biz.request.bo.JoinGroupResult;
import com.yunos.tvtaobao.biz.request.bo.LiveBonusResult;
import com.yunos.tvtaobao.biz.request.bo.LiveBonusTimeResult;
import com.yunos.tvtaobao.biz.request.bo.LiveFollowResult;
import com.yunos.tvtaobao.biz.request.bo.LiveIsFollowStatus;
import com.yunos.tvtaobao.biz.request.bo.LoadingBo;
import com.yunos.tvtaobao.biz.request.bo.MyAlipayHongbaoList;
import com.yunos.tvtaobao.biz.request.bo.MyCouponsList;
import com.yunos.tvtaobao.biz.request.bo.MyTaoBaoModule;
import com.yunos.tvtaobao.biz.request.bo.OrderDetailMO;
import com.yunos.tvtaobao.biz.request.bo.OrderListData;
import com.yunos.tvtaobao.biz.request.bo.OrderListLogistics;
import com.yunos.tvtaobao.biz.request.bo.OrderLogisticMo;
import com.yunos.tvtaobao.biz.request.bo.PaginationItemRates;
import com.yunos.tvtaobao.biz.request.bo.ProductTagBo;
import com.yunos.tvtaobao.biz.request.bo.PromotionBean;
import com.yunos.tvtaobao.biz.request.bo.QueryBagRequestBo;
import com.yunos.tvtaobao.biz.request.bo.RebateBo;
import com.yunos.tvtaobao.biz.request.bo.RelatedItem;
import com.yunos.tvtaobao.biz.request.bo.SearchResult;
import com.yunos.tvtaobao.biz.request.bo.SellerInfo;
import com.yunos.tvtaobao.biz.request.bo.ShopCoupon;
import com.yunos.tvtaobao.biz.request.bo.ShopDetailData;
import com.yunos.tvtaobao.biz.request.bo.ShopSearchResultBean;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.request.bo.TakeOutBag;
import com.yunos.tvtaobao.biz.request.bo.TakeOutBagAgain;
import com.yunos.tvtaobao.biz.request.bo.TakeOutOrderCancelData;
import com.yunos.tvtaobao.biz.request.bo.TakeOutOrderDeliveryData;
import com.yunos.tvtaobao.biz.request.bo.TakeOutOrderInfoDetails;
import com.yunos.tvtaobao.biz.request.bo.TakeOutOrderListData;
import com.yunos.tvtaobao.biz.request.bo.TakeVouchers;
import com.yunos.tvtaobao.biz.request.bo.TakeoutApplyCoupon;
import com.yunos.tvtaobao.biz.request.bo.TaobaoPointCheckinStatusBo;
import com.yunos.tvtaobao.biz.request.bo.TbItemDetail;
import com.yunos.tvtaobao.biz.request.bo.TopicsEntity;
import com.yunos.tvtaobao.biz.request.bo.TypeWordsRequestMtop;
import com.yunos.tvtaobao.biz.request.bo.ValidateLotteryBean;
import com.yunos.tvtaobao.biz.request.bo.VouchersList;
import com.yunos.tvtaobao.biz.request.bo.VouchersMO;
import com.yunos.tvtaobao.biz.request.bo.VouchersSummary;
import com.yunos.tvtaobao.biz.request.bo.WeitaoFollowBean;
import com.yunos.tvtaobao.biz.request.core.AsyncDataLoader;
import com.yunos.tvtaobao.biz.request.core.ServerTimeSynchronizer;
import com.yunos.tvtaobao.biz.request.core.ServiceCode;
import com.yunos.tvtaobao.biz.request.core.ServiceResponse;
import com.yunos.tvtaobao.biz.request.item.AddBagRequest;
import com.yunos.tvtaobao.biz.request.item.AddCollectionRequest;
import com.yunos.tvtaobao.biz.request.item.AddFollowRequest;
import com.yunos.tvtaobao.biz.request.item.AddLotteryRecordRequest;
import com.yunos.tvtaobao.biz.request.item.AdjustBuildOrderRequest;
import com.yunos.tvtaobao.biz.request.item.ApplyCouponRequest;
import com.yunos.tvtaobao.biz.request.item.ApplyShopCoupon;
import com.yunos.tvtaobao.biz.request.item.BuildOrderRequest;
import com.yunos.tvtaobao.biz.request.item.CancelCollectionRequest;
import com.yunos.tvtaobao.biz.request.item.CheckFavRequest;
import com.yunos.tvtaobao.biz.request.item.CouponReceiveRequest;
import com.yunos.tvtaobao.biz.request.item.CreateOrderRequestV3;
import com.yunos.tvtaobao.biz.request.item.DetainMentRequest;
import com.yunos.tvtaobao.biz.request.item.DoPayRequest;
import com.yunos.tvtaobao.biz.request.item.ElemeReverseGeoRequest;
import com.yunos.tvtaobao.biz.request.item.FeiZhuItemDetailNewRequest;
import com.yunos.tvtaobao.biz.request.item.FeiZhuItemDetailRequest;
import com.yunos.tvtaobao.biz.request.item.GetAddressListRequest;
import com.yunos.tvtaobao.biz.request.item.GetAdvertsRequest;
import com.yunos.tvtaobao.biz.request.item.GetBuildOrderRebateMoneyRequest;
import com.yunos.tvtaobao.biz.request.item.GetBuyToCashbackRequest;
import com.yunos.tvtaobao.biz.request.item.GetByTaobaoUserIdRequest;
import com.yunos.tvtaobao.biz.request.item.GetCPSBonus;
import com.yunos.tvtaobao.biz.request.item.GetCartStyleRequest;
import com.yunos.tvtaobao.biz.request.item.GetCollectsRequest;
import com.yunos.tvtaobao.biz.request.item.GetCouponListRequest;
import com.yunos.tvtaobao.biz.request.item.GetCouponRecommendList;
import com.yunos.tvtaobao.biz.request.item.GetDeviceIdRequest;
import com.yunos.tvtaobao.biz.request.item.GetDoTbItemCouponRequest;
import com.yunos.tvtaobao.biz.request.item.GetDynamicRecommendRequest;
import com.yunos.tvtaobao.biz.request.item.GetElemeRecommandBindRequest;
import com.yunos.tvtaobao.biz.request.item.GetElemeRecommandRequest;
import com.yunos.tvtaobao.biz.request.item.GetElemeRegisterAndBindRequest;
import com.yunos.tvtaobao.biz.request.item.GetElemeTaobaoPhoneRequest;
import com.yunos.tvtaobao.biz.request.item.GetFindSameRequest;
import com.yunos.tvtaobao.biz.request.item.GetFollowRequest;
import com.yunos.tvtaobao.biz.request.item.GetFootMarkRequest;
import com.yunos.tvtaobao.biz.request.item.GetFootMarkSummationRequest;
import com.yunos.tvtaobao.biz.request.item.GetFullItemDescRequest;
import com.yunos.tvtaobao.biz.request.item.GetGuessLikeRequest;
import com.yunos.tvtaobao.biz.request.item.GetHomeGuessLikeRequest;
import com.yunos.tvtaobao.biz.request.item.GetHotWordsRequest;
import com.yunos.tvtaobao.biz.request.item.GetItemDetailV5Request;
import com.yunos.tvtaobao.biz.request.item.GetItemDetailV6Request;
import com.yunos.tvtaobao.biz.request.item.GetItemRatesRequest;
import com.yunos.tvtaobao.biz.request.item.GetJhsItemDetailRequest;
import com.yunos.tvtaobao.biz.request.item.GetLiveBonusResultRequest;
import com.yunos.tvtaobao.biz.request.item.GetLiveBonusTimeRequest;
import com.yunos.tvtaobao.biz.request.item.GetLiveCancelFollowResultRequest;
import com.yunos.tvtaobao.biz.request.item.GetLiveFollowResultRequest;
import com.yunos.tvtaobao.biz.request.item.GetLiveIsFollowStatusRequest;
import com.yunos.tvtaobao.biz.request.item.GetLogisticsRequest;
import com.yunos.tvtaobao.biz.request.item.GetMyAlipayHongbaoList;
import com.yunos.tvtaobao.biz.request.item.GetMyCouponsListRequest;
import com.yunos.tvtaobao.biz.request.item.GetMyFollowRequest;
import com.yunos.tvtaobao.biz.request.item.GetNewCollectsNumRequest;
import com.yunos.tvtaobao.biz.request.item.GetNewCollectsRequest;
import com.yunos.tvtaobao.biz.request.item.GetNormalJhsListRequest;
import com.yunos.tvtaobao.biz.request.item.GetOauthUrlRequest;
import com.yunos.tvtaobao.biz.request.item.GetOrderDetailRequest;
import com.yunos.tvtaobao.biz.request.item.GetOrderListRequest;
import com.yunos.tvtaobao.biz.request.item.GetOrderLogistic;
import com.yunos.tvtaobao.biz.request.item.GetProductTagRequest;
import com.yunos.tvtaobao.biz.request.item.GetPromotionForready;
import com.yunos.tvtaobao.biz.request.item.GetRealTimeRecommondRequest;
import com.yunos.tvtaobao.biz.request.item.GetRebateMoneyRequest;
import com.yunos.tvtaobao.biz.request.item.GetSearchResultRequest;
import com.yunos.tvtaobao.biz.request.item.GetSearhRelationRecommendRequest;
import com.yunos.tvtaobao.biz.request.item.GetServerTimeRequest;
import com.yunos.tvtaobao.biz.request.item.GetShopCatInfoRequest;
import com.yunos.tvtaobao.biz.request.item.GetShopCoupon;
import com.yunos.tvtaobao.biz.request.item.GetShopItemListRequest;
import com.yunos.tvtaobao.biz.request.item.GetStdCats;
import com.yunos.tvtaobao.biz.request.item.GetSuperChoiceJhsListRequest;
import com.yunos.tvtaobao.biz.request.item.GetTaobaoPoint;
import com.yunos.tvtaobao.biz.request.item.GetTaobaoPointCheckinStatus;
import com.yunos.tvtaobao.biz.request.item.GetTaobaoPointSign;
import com.yunos.tvtaobao.biz.request.item.GetTaobaoPointValidateblackfilter;
import com.yunos.tvtaobao.biz.request.item.GetTbItemCouponInfoRequest;
import com.yunos.tvtaobao.biz.request.item.GetTbItemDetailRequest;
import com.yunos.tvtaobao.biz.request.item.GetWapRelatedItems;
import com.yunos.tvtaobao.biz.request.item.GetWapShopInfoRequest;
import com.yunos.tvtaobao.biz.request.item.GuessLikeRequest;
import com.yunos.tvtaobao.biz.request.item.HasActiviteRequest;
import com.yunos.tvtaobao.biz.request.item.JoinGroupRequest;
import com.yunos.tvtaobao.biz.request.item.JumpUrlRequest;
import com.yunos.tvtaobao.biz.request.item.LogReceive;
import com.yunos.tvtaobao.biz.request.item.MTopBounsRequest;
import com.yunos.tvtaobao.biz.request.item.ManageFavRequest;
import com.yunos.tvtaobao.biz.request.item.QueryBagRequest;
import com.yunos.tvtaobao.biz.request.item.QueryCreateTvTaoOrderRequest;
import com.yunos.tvtaobao.biz.request.item.RemoveFollowRequest;
import com.yunos.tvtaobao.biz.request.item.SearchRequest;
import com.yunos.tvtaobao.biz.request.item.SearchRequestMtop;
import com.yunos.tvtaobao.biz.request.item.SearchRequestMtopZtc;
import com.yunos.tvtaobao.biz.request.item.SearchResultRequest;
import com.yunos.tvtaobao.biz.request.item.SetDefaultAddressRequest;
import com.yunos.tvtaobao.biz.request.item.ShopDetailDataRequest;
import com.yunos.tvtaobao.biz.request.item.ShopFavoritesRequest;
import com.yunos.tvtaobao.biz.request.item.ShopSearchRequest;
import com.yunos.tvtaobao.biz.request.item.TBDetailV6HttpDetailDialogRequest;
import com.yunos.tvtaobao.biz.request.item.TBDetailV6HttpRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutAddBagRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutAgainRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutCancelOrderRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutCouponStyle;
import com.yunos.tvtaobao.biz.request.item.TakeOutCouponStyleRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutGetBagRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutGetGaodeDistanceRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutGetOrderDeliveryRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutGetOrderDetailRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutGetOrderListRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutGetVouchersRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutUpdateBagRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutVouchersListRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutVouchersRequest;
import com.yunos.tvtaobao.biz.request.item.TakeOutVouchersSummaryRequest;
import com.yunos.tvtaobao.biz.request.item.TakeoutApplyCouponRequest;
import com.yunos.tvtaobao.biz.request.item.TaokeDetailAnalysisRequest;
import com.yunos.tvtaobao.biz.request.item.TaokeJHSListAnalysisRequest;
import com.yunos.tvtaobao.biz.request.item.TaokeLoginAnalysisRequest;
import com.yunos.tvtaobao.biz.request.item.TopicsTmsRequest;
import com.yunos.tvtaobao.biz.request.item.TypeWordsRequest;
import com.yunos.tvtaobao.biz.request.item.UpdateBagRequest;
import com.yunos.tvtaobao.biz.request.item.UpgradeApp;
import com.yunos.tvtaobao.biz.request.item.UpgradeNewFeature;
import com.yunos.tvtaobao.biz.request.item.ValidateLotteryRequest;
import com.yunos.tvtaobao.biz.request.item.VideoJsonRequest;
import com.yunos.tvtaobao.biz.request.item.VoiceRegisterRequest;
import com.yunos.tvtaobao.biz.request.item.WeitaoAddRequest;
import com.yunos.tvtaobao.biz.request.item.WeitaoFollowRequest;
import com.yunos.tvtaobao.biz.request.item.WeitaoRemoveRequest;
import com.yunos.tvtaobao.biz.request.item.WeitaoSetDynamic;
import com.yunos.tvtaobao.biz.request.item.appearMACAndEventRequest;
import com.yunos.tvtaobao.payment.utils.ErrorReport;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.domain.MtopResponse;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class BusinessRequest {
    public static final Long RECOMMEND_CATEGORY_ID = 0L;
    private static BusinessRequest mBusinessRequest;
    protected String TAG = getClass().getSimpleName();
    private MtopResponseListener mtopResponseListener;

    public interface MtopResponseListener {
        void onMtopResponse(MtopResponse mtopResponse);
    }

    public interface RequestLoadListener<T> {
        ServiceResponse<T> load();
    }

    public MtopResponseListener getMtopResponseListener() {
        return this.mtopResponseListener;
    }

    public void setMtopResponseListener(MtopResponseListener mtopResponseListener2) {
        this.mtopResponseListener = mtopResponseListener2;
    }

    public static BusinessRequest getBusinessRequest() {
        if (mBusinessRequest == null) {
            mBusinessRequest = new BusinessRequest();
        }
        return mBusinessRequest;
    }

    public void destory() {
        mBusinessRequest = null;
    }

    public <T> void baseRequest(final RequestLoadListener<T> requestLoadListener, final RequestListener<T> listener, boolean needLogin) {
        if (needLogin) {
            AsyncDataLoader.execute(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                public ServiceResponse<T> load() {
                    return requestLoadListener.load();
                }

                public void postExecute(ServiceResponse<T> result) {
                    BusinessRequest.this.normalPostExecute(result, listener);
                }

                public void onStartLogin() {
                    if (listener != null && (listener instanceof RequestListener.RequestListenerWithLogin)) {
                        ((RequestListener.RequestListenerWithLogin) listener).onStartLogin();
                    }
                }

                public void preExecute() {
                }
            });
        } else {
            AsyncDataLoader.executeWithNoAutoLogin(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                public ServiceResponse<T> load() {
                    return requestLoadListener.load();
                }

                public void postExecute(ServiceResponse<T> result) {
                    BusinessRequest.this.normalPostExecute(result, listener);
                }

                public void onStartLogin() {
                    if (listener != null && (listener instanceof RequestListener.RequestListenerWithLogin)) {
                        ((RequestListener.RequestListenerWithLogin) listener).onStartLogin();
                    }
                }

                public void preExecute() {
                }
            });
        }
    }

    public <T> void baseRequest(BaseHttpRequest request, RequestListener<T> listener, boolean needLogin) {
        baseRequest(request, listener, needLogin, false);
    }

    public <T> void baseRequest(final BaseHttpRequest request, final RequestListener<T> listener, boolean needLogin, final boolean post) {
        if (request != null) {
            if (needLogin) {
                AsyncDataLoader.execute(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                    public ServiceResponse<T> load() {
                        return BusinessRequest.this.processHttpRequest(request, post);
                    }

                    public void postExecute(ServiceResponse<T> result) {
                        BusinessRequest.this.normalPostExecute(result, listener);
                    }

                    public void onStartLogin() {
                        if (listener != null && (listener instanceof RequestListener.RequestListenerWithLogin)) {
                            ((RequestListener.RequestListenerWithLogin) listener).onStartLogin();
                        }
                    }

                    public void preExecute() {
                    }
                });
            } else {
                AsyncDataLoader.executeWithNoAutoLogin(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                    public ServiceResponse<T> load() {
                        return BusinessRequest.this.processHttpRequest(request, post);
                    }

                    public void postExecute(ServiceResponse<T> result) {
                        BusinessRequest.this.normalPostExecute(result, listener);
                    }

                    public void onStartLogin() {
                        if (listener != null && (listener instanceof RequestListener.RequestListenerWithLogin)) {
                            ((RequestListener.RequestListenerWithLogin) listener).onStartLogin();
                        }
                    }

                    public void preExecute() {
                    }
                });
            }
        }
    }

    public <T> void baseRequest(BaseMtopRequest request, RequestListener<T> listener, boolean needLogin) {
        baseRequest(request, listener, needLogin, false, 0);
    }

    public <T> void baseRequest(BaseMtopRequest request, RequestListener<T> listener, boolean needLogin, int timeout) {
        baseRequest(request, listener, needLogin, false, timeout);
    }

    public <T> void baseRequest(BaseMtopRequest request, RequestListener<T> listener, boolean needLogin, boolean post) {
        baseRequest(request, listener, needLogin, post, 0);
    }

    public <T> void baseRequest(BaseMtopRequest request, RequestListener<T> listener, boolean needLogin, boolean post, int timeout) {
        if (request != null) {
            if (needLogin) {
                final BaseMtopRequest baseMtopRequest = request;
                final boolean z = post;
                final int i = timeout;
                final RequestListener<T> requestListener = listener;
                AsyncDataLoader.execute(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                    public ServiceResponse<T> load() {
                        return BusinessRequest.this.normalLoad(baseMtopRequest, z, i);
                    }

                    public void postExecute(ServiceResponse<T> result) {
                        BusinessRequest.this.normalPostExecute(result, requestListener);
                    }

                    public void onStartLogin() {
                        if (requestListener != null && (requestListener instanceof RequestListener.RequestListenerWithLogin)) {
                            ((RequestListener.RequestListenerWithLogin) requestListener).onStartLogin();
                        }
                    }

                    public void preExecute() {
                    }
                });
                return;
            }
            final BaseMtopRequest baseMtopRequest2 = request;
            final boolean z2 = post;
            final int i2 = timeout;
            final RequestListener<T> requestListener2 = listener;
            AsyncDataLoader.executeWithNoAutoLogin(new AsyncDataLoader.DataLoadCallback<ServiceResponse<T>>() {
                public ServiceResponse<T> load() {
                    return BusinessRequest.this.normalLoad(baseMtopRequest2, z2, i2);
                }

                public void postExecute(ServiceResponse<T> result) {
                    BusinessRequest.this.normalPostExecute(result, requestListener2);
                }

                public void onStartLogin() {
                    if (requestListener2 != null && (requestListener2 instanceof RequestListener.RequestListenerWithLogin)) {
                        ((RequestListener.RequestListenerWithLogin) requestListener2).onStartLogin();
                    }
                }

                public void preExecute() {
                }
            });
        }
    }

    public <T> void baseSyncRequest(BaseMtopRequest request, RequestListener<T> listener, boolean post) {
        if (request != null) {
            normalPostExecute(normalLoad(request, post, 0), listener);
        }
    }

    public void requestVoiceRegister(String referrer, String className, String type, String params, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new VoiceRegisterRequest(referrer, className, type, params), listener, false, true);
    }

    public void requestSearch(Integer pageSize, Integer curPage, String kw, String sort, String nid, String tab, RequestListener<GoodsSearchMO> listener) {
        requestSearch(pageSize, curPage, kw, sort, nid, (String) null, tab, listener);
    }

    public void requestSearch(Integer pageSize, Integer curPage, String kw, String sort, String nid, String catmap, String tab, RequestListener<GoodsSearchMO> listener) {
        SearchRequest request = new SearchRequest();
        request.setCurPage(curPage.intValue());
        request.setKw(kw);
        request.setPageSize(pageSize.intValue());
        request.setSort(sort);
        request.setNid(nid);
        request.setCatmap(catmap);
        request.setTab(tab);
        baseRequest((BaseHttpRequest) request, listener, false);
    }

    public void reverseElemeGeo(String latitude, String longitude, RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new ElemeReverseGeoRequest(latitude, longitude), listener, false);
    }

    public void getAdvertsRequest(String uuid, String loadVersion, String extParams, RequestListener<List<LoadingBo>> listener) {
        baseRequest((BaseMtopRequest) new GetAdvertsRequest(uuid, loadVersion, extParams), listener, false);
    }

    public void requestDetainMent(String userId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new DetainMentRequest(userId), listener, false);
    }

    public void requestTypeWords(String orgin, RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new TypeWordsRequest(orgin), listener, false);
    }

    public void requestJumpUrl(RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new JumpUrlRequest(), listener, false);
    }

    public void requestHasActivite(RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new HasActiviteRequest(), listener, false);
    }

    public void requestVideoJson(RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new VideoJsonRequest(), listener, false);
    }

    public void requestBounsByMTop(RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new MTopBounsRequest(), listener, false);
    }

    public void requestQueryCreateTvTaoOrderRequest(String itemNumId, String orderId, String cartFlag, String channel, String versionName, String extParams, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new QueryCreateTvTaoOrderRequest(itemNumId, orderId, cartFlag, channel, versionName, extParams), listener, false);
    }

    public void requestSearchMtop(String q, Integer page_size, Integer page_no, String sort, String cat, String category, RequestListener<GoodsSearchResult> listener) {
        requestSearchMtop(q, page_size, page_no, sort, cat, category, (String) null, (String) null, (String) null, (String) null, (String) null, "tvtaobao", (String) null, listener);
    }

    public void requestSearchMtop(String q, Integer page_size, Integer page_no, String sort, String cat, String category, String prop, String start_price, String end_price, String item_ids, String user_id, String from, String auction_tag, RequestListener<GoodsSearchResult> listener) {
        SearchRequestMtop searchRequest = new SearchRequestMtop();
        searchRequest.setQueryKW(q);
        searchRequest.setPageSize(page_size.intValue());
        searchRequest.setPageNo(page_no.intValue());
        searchRequest.setSort(sort);
        searchRequest.setCat(cat);
        searchRequest.setCategory(category);
        searchRequest.setProp(prop);
        searchRequest.setStartPrice(start_price);
        searchRequest.setEndPrice(end_price);
        searchRequest.setItemIds(item_ids);
        searchRequest.setUserId(user_id);
        searchRequest.setFrom(from);
        searchRequest.setAuctionTag(auction_tag);
        baseRequest((BaseMtopRequest) searchRequest, listener, false);
    }

    public void requestSearchMtopZTC(String q, Integer page_size, Integer page_no, int per, String sort, String cat, String category, String item_ids, String ip, String flag, int ztcC, RequestListener<GoodsSearchZtcResult> listener) {
        SearchRequestMtopZtc searchRequest = new SearchRequestMtopZtc();
        searchRequest.setQueryKW(q);
        searchRequest.setPageSize(page_size.intValue());
        searchRequest.setPageNo(page_no.intValue());
        searchRequest.setFlag(flag);
        searchRequest.setPer(per);
        searchRequest.setIp(ip);
        searchRequest.setZtcc(ztcC);
        searchRequest.setCateId(cat);
        searchRequest.setSort(sort);
        searchRequest.setItemIds(item_ids);
        baseRequest((BaseMtopRequest) searchRequest, listener, false);
    }

    public void requestSearchResult(String q, Integer page_size, Integer page_no, int per, String sort, String cat, String flag, List<String> list, boolean isFromCartToBuildOrder, boolean isFromBuildOrder, boolean isTmail, RequestListener<GoodsSearchResultDo> listener) {
        SearchResultRequest searchRequest = new SearchResultRequest(list, isFromCartToBuildOrder, isFromBuildOrder, isTmail);
        searchRequest.setQueryKW(q);
        searchRequest.setPageSize(page_size.intValue());
        searchRequest.setPageNo(page_no.intValue());
        searchRequest.setFlag(flag);
        searchRequest.setPer(per);
        searchRequest.setCateId(cat);
        searchRequest.setSort(sort);
        baseRequest((BaseMtopRequest) searchRequest, listener, false);
    }

    public void requestUpdatServerTime(RequestListener<Long> listener) {
        if (NetWorkUtil.isNetWorkAvailable() && !ServerTimeSynchronizer.isServerTime()) {
            baseRequest((BaseMtopRequest) new GetServerTimeRequest(), listener, false);
        }
    }

    public void requestSyncUpdatServerTime(RequestListener<Long> listener) {
        if (NetWorkUtil.isNetWorkAvailable() && !ServerTimeSynchronizer.isServerTime()) {
            baseSyncRequest(new GetServerTimeRequest(), listener, false);
        }
    }

    public void requestGetItemDetailV6(String itemNumId, String extParam, RequestListener<TBDetailResultV6> listener) {
        ZpLogger.i("wordlbin", "requestGetItemDetailV6: itemid= " + itemNumId + " extparam= " + extParam);
        baseRequest((BaseMtopRequest) new GetItemDetailV6Request(itemNumId, extParam), listener, false);
    }

    public void requestGetItemDetailV6New(String itemNumId, String areaId, RequestListener<TBDetailResultV6> listener) {
        ZpLogger.i("wordlbin", "requestGetItemDetailV6: itemid= " + itemNumId + " areaId= " + areaId);
        baseRequest((BaseMtopRequest) new TBDetailV6HttpRequest(itemNumId, areaId), listener, false);
    }

    public void requestGetItemDetailV6NewDetailDialog(String itemNumId, String areaId, RequestListener<com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.TBDetailResultV6> listener) {
        ZpLogger.i("wordlbin", "requestGetItemDetailV6: itemid= " + itemNumId + " areaId= " + areaId);
        baseRequest((BaseMtopRequest) new TBDetailV6HttpDetailDialogRequest(itemNumId, areaId), listener, false);
    }

    public void requestGetFeiZhuItemDetail(String itemid, RequestListener<FeiZhuBean> listener) {
        baseRequest((BaseMtopRequest) new FeiZhuItemDetailRequest(itemid), listener, false);
    }

    public void requestGetFeiZhuItemDetailNew(String itemid, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new FeiZhuItemDetailNewRequest(itemid), listener, false);
    }

    public void requestGetItemDetailV5(final String itemId, final String extParam, RequestListener<TBDetailResultVO> listener) {
        baseRequest(new RequestLoadListener<TBDetailResultVO>() {
            public ServiceResponse<TBDetailResultVO> load() {
                ServiceResponse<TBDetailResultVO> resolveResponse;
                Map<String, String> params = new HashMap<>();
                params.put("id", itemId);
                DetailConfig.ttid = Config.getTTid();
                if (Config.getRunMode() == RunMode.DAILY) {
                    DetailConfig.env = DetailConfig.WAPTEST;
                }
                GetItemDetailV5Request detailRequest = new GetItemDetailV5Request(itemId, extParam);
                Network network = new DegradableNetwork(CoreApplication.getApplication());
                String url = detailRequest.getUrl();
                Response response = network.syncSend(new RequestImpl(url), (Object) null);
                int statusCode = response.getStatusCode();
                Map<String, List<String>> head = response.getConnHeadFields();
                byte[] data = response.getBytedata();
                StatisticData staticData = response.getStatisticData();
                if (data != null) {
                    try {
                        resolveResponse = detailRequest.resolveResponse(new String(data));
                    } catch (Exception e) {
                        resolveResponse = new ServiceResponse<>();
                        resolveResponse.update(ServiceCode.DATA_PARSE_ERROR);
                        e.printStackTrace();
                    }
                } else {
                    resolveResponse = new ServiceResponse<>();
                    resolveResponse.update(ServiceCode.HTTP_ERROR);
                }
                ZpLogger.v(BusinessRequest.this.TAG, ".requestGetItemDetailV5.url = " + url);
                ZpLogger.v(BusinessRequest.this.TAG, ".requestGetItemDetailV5.response = " + response);
                ZpLogger.v(BusinessRequest.this.TAG, ".requestGetItemDetailV5.statusCode = " + statusCode);
                ZpLogger.v(BusinessRequest.this.TAG, ".requestGetItemDetailV5.head = " + head);
                ZpLogger.v(BusinessRequest.this.TAG, ".requestGetItemDetailV5.data = " + data);
                ZpLogger.v(BusinessRequest.this.TAG, ".requestGetItemDetailV5.staticData = " + staticData);
                ZpLogger.v(BusinessRequest.this.TAG, ".requestGetItemDetailV5.resolveResponse = " + resolveResponse);
                final ServiceResponse<TBDetailResultVO> serviceResponse = resolveResponse;
                try {
                    serviceResponse.setData(DetailApiProxy.synRequest(params, new DetailApiRequestor() {
                        public TBDetailResultVO syncRequest(Unit apiUnit) {
                            return (TBDetailResultVO) serviceResponse.getData();
                        }
                    }));
                } catch (Exception e2) {
                    e2.printStackTrace();
                    serviceResponse.setData(null);
                }
                return serviceResponse;
            }
        }, listener, false);
    }

    public void requestGetItemDetail(Long itemId, RequestListener<TbItemDetail> listener) {
        baseRequest((BaseMtopRequest) new GetTbItemDetailRequest(itemId), listener, false);
    }

    public void requestGetJhsItemDetail(Long juId, RequestListener<JhsItemDetail> listener) {
        baseRequest((BaseMtopRequest) new GetJhsItemDetailRequest(juId), listener, false);
    }

    public void requestGetAddressList(RequestListener<List<Address>> listener) {
        baseRequest((BaseMtopRequest) new GetAddressListRequest(), listener, true);
    }

    public void requestSetDefaultAddress(String deliverId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new SetDefaultAddressRequest(deliverId), listener, true);
    }

    public void requestJoinGroup(String itemId, RequestListener<JoinGroupResult> listener) {
        baseRequest((BaseMtopRequest) new JoinGroupRequest(itemId), listener, true);
    }

    public void requestProductTag(String itemId, List<String> list, boolean isZTC, String source, boolean isPre, String amount, Context context, RequestListener<ProductTagBo> listener) {
        try {
            JSONObject object = new JSONObject();
            object.put("umToken", Config.getUmtoken(context));
            object.put("wua", Config.getWua(context));
            object.put("isSimulator", Config.isSimulator(context));
            object.put("userAgent", Config.getAndroidSystem(context));
            baseRequest((BaseMtopRequest) new GetProductTagRequest(itemId, list, isZTC, source, isPre, amount, object.toString()), listener, false, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestRebateMoney(String itemIdArray, List<String> list, boolean isFromCartToBuildOrder, boolean isFromBuildOrder, boolean mjf, String extParams, RequestListener<List<RebateBo>> listener) {
        baseRequest((BaseMtopRequest) new GetRebateMoneyRequest(itemIdArray, list, isFromCartToBuildOrder, isFromBuildOrder, mjf, extParams), listener, false, true);
    }

    public void requestBuildOrderRebateMoney(String infoArray, String extParams, RequestListener<List<BuildOrderRebateBo>> listener) {
        baseRequest((BaseMtopRequest) new GetBuildOrderRebateMoneyRequest(infoArray, extParams), listener, false, true);
    }

    public void requestBuyToCashback(String itemIdArray, RequestListener<Map<String, String>> listener) {
        baseRequest((BaseMtopRequest) new GetBuyToCashbackRequest(itemIdArray), listener, false, true);
    }

    public void buildOrder(BuildOrderRequestBo request, boolean hasAddCart, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new BuildOrderRequest(request, hasAddCart), listener, true, true);
    }

    public void adjustBuildOrder(String params, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new AdjustBuildOrderRequest(params), listener, true, true);
    }

    public void createOrder(String params, RequestListener<CreateOrderResult> listener) {
        baseRequest((BaseMtopRequest) new CreateOrderRequestV3(params), listener, true, true);
    }

    public void requestOrderDetail(Long orderId, RequestListener<OrderDetailMO> listener) {
        baseRequest((BaseMtopRequest) new GetOrderDetailRequest(orderId), listener, true);
    }

    public void doPay(Long orderId, RequestListener<DoPayOrders> listener) {
        baseRequest((BaseMtopRequest) new DoPayRequest(orderId), listener, true);
    }

    public void requestOrderLogistic(Long orderId, RequestListener<OrderLogisticMo> listener) {
        baseRequest((BaseMtopRequest) new GetOrderLogistic(orderId), listener, true);
    }

    public void requestGetDeviceId(RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new GetDeviceIdRequest(), listener, false);
    }

    public void checkFav(String itemId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new CheckFavRequest(itemId), listener, true);
    }

    public void getCollects(int page, int pageSize, RequestListener<CollectList> listener) {
        baseRequest((BaseMtopRequest) new GetCollectsRequest(page, pageSize), listener, true);
    }

    public void getNewCollects(String startTime, int pageSize, RequestListener<CollectionsInfo> listener) {
        baseRequest((BaseMtopRequest) new GetNewCollectsRequest(startTime, pageSize), listener, true);
    }

    public void getCollectsAndFollowNum(int waitReceiveOrderCnt, RequestListener<MyTaoBaoModule> listener) {
        baseRequest((BaseMtopRequest) new GetNewCollectsNumRequest(waitReceiveOrderCnt), listener, true);
    }

    public void manageFav(String itemId, String func, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new ManageFavRequest(itemId, func), listener, true);
    }

    public void addCollection(String itemId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new AddCollectionRequest(itemId), listener, true);
    }

    public void cancelCollection(String itemId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new CancelCollectionRequest(itemId), listener, true);
    }

    public void requestGetItemCouponInfo(String itemId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new GetTbItemCouponInfoRequest(itemId), listener, false);
    }

    public void requestDoItemCoupon(String itemId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new GetDoTbItemCouponRequest(itemId), listener, true);
    }

    public void requestGetFullItemDesc(String itemId, RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new GetFullItemDescRequest(itemId), listener, false);
    }

    public void requestGetItemRates(String itemId, int pageNo, int pageSize, String rateType, RequestListener<PaginationItemRates> listener) {
        baseRequest((BaseMtopRequest) new GetItemRatesRequest(itemId, pageNo, pageSize, rateType), listener, false);
    }

    public void requestGetOrderCount(String tabCode, RequestListener<OrderListData> listener) {
        baseRequest((BaseMtopRequest) new GetOrderListRequest(tabCode), listener, true, true);
    }

    public void getShopItemList(String uid, String shopId, String sort, String newDays, String catId, String q, String startPrice, String endPrice, String pageSize, String currentPage, RequestListener<GoodsList> listener) {
        baseRequest((BaseMtopRequest) new GetShopItemListRequest(uid, shopId, sort, newDays, catId, q, startPrice, endPrice, pageSize, currentPage), listener, false);
    }

    public void getWapShopInfo(String sellerId, String shopId, RequestListener<SellerInfo> listener) {
        baseRequest((BaseMtopRequest) new GetWapShopInfoRequest(sellerId, shopId), listener, false);
    }

    public void getCatInfoInShop(String sellerId, String shopId, RequestListener<List<Cat>> listener) {
        baseRequest((BaseMtopRequest) new GetShopCatInfoRequest(sellerId, shopId), listener, false);
    }

    public void getCouponList(int page, int pageSize, String tag, RequestListener<CouponList> listener) {
        baseRequest((BaseMtopRequest) new GetCouponListRequest(page, pageSize, tag), listener, true, true);
    }

    public void getTaobaoPointValidateblackfilter(String catergoryId, String itemId, RequestListener<Boolean> listener) {
        baseRequest((BaseMtopRequest) new GetTaobaoPointValidateblackfilter(catergoryId, itemId), listener, false);
    }

    public void getstdcats(String catergoryId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new GetStdCats(catergoryId), listener, false);
    }

    public void requestMyCouponsList(String bizType, String couponType, RequestListener<MyCouponsList> listener) {
        baseRequest((BaseMtopRequest) new GetMyCouponsListRequest(bizType, couponType), listener, true);
    }

    public void requestTaobaoPoint(RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new GetTaobaoPoint(), listener, false);
    }

    public void requestTaobaoPointCheckinStatus(RequestListener<TaobaoPointCheckinStatusBo> listener) {
        baseRequest((BaseMtopRequest) new GetTaobaoPointCheckinStatus(), listener, true);
    }

    public void requestTaobaoPointSign(RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new GetTaobaoPointSign(), listener, true);
    }

    public void requestMyAlipayHongbaoList(String currentPage, RequestListener<MyAlipayHongbaoList> listener) {
        baseRequest((BaseMtopRequest) new GetMyAlipayHongbaoList(currentPage), listener, true);
    }

    public void requestSingleLogisticsInfo(String orderId, RequestListener<List<OrderListLogistics>> listener) {
        baseRequest((BaseMtopRequest) new GetLogisticsRequest(orderId), listener, true);
    }

    public void requestCouponRecommendList(String sellerId, String couponId, RequestListener<CouponRecommendList> listener) {
        baseRequest((BaseMtopRequest) new GetCouponRecommendList(sellerId, couponId), listener, true);
    }

    public void topicsTmsRequest(String url, RequestListener<TopicsEntity> listener) {
        baseRequest((BaseHttpRequest) new TopicsTmsRequest(url), listener, false);
    }

    public void getHotWordsList(String type, RequestListener<ArrayList<String>> listener) {
        baseRequest((BaseMtopRequest) new GetHotWordsRequest(type), listener, false);
    }

    public void getBonusData(String refpid, String e, String wua, String asac, RequestListener<BonusBean> listener) {
        baseRequest((BaseMtopRequest) new GetCPSBonus(refpid, e, wua, asac), listener, true);
    }

    public void getValidateLottery(String uuid, RequestListener<ValidateLotteryBean> listener) {
        baseRequest((BaseMtopRequest) new ValidateLotteryRequest(uuid), listener, true);
    }

    public void getLiveBonusTime(RequestListener<LiveBonusTimeResult> listener) {
        baseRequest((BaseMtopRequest) new GetLiveBonusTimeRequest(), listener, false);
    }

    public void getLiveBonusResult(String bizId, String type, String asac, RequestListener<LiveBonusResult> listener) {
        baseRequest((BaseMtopRequest) new GetLiveBonusResultRequest(bizId, type, asac), listener, true);
    }

    public void getLiveFollowResult(String followedId, RequestListener<LiveFollowResult> listener) {
        baseRequest((BaseMtopRequest) new GetLiveFollowResultRequest(followedId), listener, true);
    }

    public void getLiveCancelFollowResult(String followedId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new GetLiveCancelFollowResultRequest(followedId), listener, true);
    }

    public void getLiveIsFollowStatus(String followedId, RequestListener<LiveIsFollowStatus> listener) {
        baseRequest((BaseMtopRequest) new GetLiveIsFollowStatusRequest(followedId), listener, true);
    }

    public void addLotteryRecord(String amount, String uuid) {
        baseRequest((BaseMtopRequest) new AddLotteryRecordRequest(amount, uuid), (RequestListener) null, true);
    }

    public void getPromotionForready(RequestListener<PromotionBean> listener) {
        baseRequest((BaseMtopRequest) new GetPromotionForready(), listener, false);
    }

    public void getTypeWordsMtops(String origin, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new TypeWordsRequestMtop(origin), listener, false);
    }

    public void appearMACAndEvent(String appKey, String o2oShopId, String metaType, String currentMeta, String duration, String routerMAC, String timestamp, String signature, String signVersion, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new appearMACAndEventRequest(appKey, o2oShopId, metaType, currentMeta, duration, routerMAC, timestamp, signature, signVersion), listener, false);
    }

    public void requestGetTbSearchResultList(String q, String area, String code, int n, RequestListener<ArrayList<String>> listener) {
        baseRequest((BaseHttpRequest) new GetSearchResultRequest(q, area, code, n), listener, false);
    }

    public void requestGetSearhRelationRecommend(String key, String utdid, RequestListener<ArrayList<String>> listener) {
        baseRequest((BaseMtopRequest) new GetSearhRelationRecommendRequest(key, utdid), listener, false);
    }

    public void addBag(String itemId, int quantity, String skuId, String extParams, RequestListener<ArrayList<SearchResult>> listener) {
        baseRequest((BaseMtopRequest) new AddBagRequest(itemId, quantity, skuId, extParams), listener, true, true);
    }

    public void findSame(int pageSize, int currentPage, String catid, String nid, RequestListener<FindSameContainerBean> listener) {
        baseRequest((BaseMtopRequest) new GetFindSameRequest(Integer.valueOf(pageSize), Integer.valueOf(currentPage), catid, nid), listener, true, true);
    }

    public void getDistanceFromGaode(String org2, String dest, RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new TakeOutGetGaodeDistanceRequest(org2, dest), listener, false, true);
    }

    public void getTakeOutOrderList(int pageNo, RequestListener<TakeOutOrderListData> listener) {
        baseRequest((BaseMtopRequest) new TakeOutGetOrderListRequest(pageNo), listener, true, true);
    }

    public void cancelTakeOutOrder(String mainOrderId, RequestListener<TakeOutOrderCancelData> listener) {
        baseRequest((BaseMtopRequest) new TakeOutCancelOrderRequest(mainOrderId), listener, true, true);
    }

    public void getTakeOutOrderDelivery(String mainOrderId, RequestListener<TakeOutOrderDeliveryData> listener) {
        baseRequest((BaseMtopRequest) new TakeOutGetOrderDeliveryRequest(mainOrderId), listener, true, true);
    }

    public void getTakeOutOrderDetail(String mainOrderId, RequestListener<TakeOutOrderInfoDetails> listener) {
        baseRequest((BaseMtopRequest) new TakeOutGetOrderDetailRequest(mainOrderId), listener, true, true);
    }

    public void queryBag(QueryBagRequestBo queryBagRequestBo, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new QueryBagRequest(queryBagRequestBo), listener, true, true);
    }

    public void guessLike(String channel, RequestListener<GuessLikeGoodsBean> listener) {
        baseRequest((BaseMtopRequest) new GetGuessLikeRequest(channel), listener, false, true);
    }

    public void getGuseeLike(String business, String activityCode, int position, RequestListener<List<GuessLikeGoodsData>> listener) {
        baseRequest((BaseMtopRequest) new GuessLikeRequest(business, activityCode, position), listener, false);
    }

    public void getCartStyle(RequestListener<CartStyleBean> listener) {
        baseRequest((BaseMtopRequest) new GetCartStyleRequest(), listener, false, true);
    }

    public void homeGuessLike(String channel, RequestListener<GuessLikeGoodsBean> listener) {
        baseRequest((BaseMtopRequest) new GetHomeGuessLikeRequest(channel), listener, false, true);
    }

    public void realTimeRecommond(RequestListener<GuessLikeGoodsBean> listener) {
        baseRequest((BaseMtopRequest) new GetRealTimeRecommondRequest(), listener, true, true);
    }

    public void getDynamicRecommendData(RequestListener<DynamicRecommend> listener) {
        baseRequest((BaseMtopRequest) new GetDynamicRecommendRequest(), listener, true, true);
    }

    public void updateBag(String params, String cartFrom, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new UpdateBagRequest(params, cartFrom), listener, true, true);
    }

    public void getWapRelatedItems(String itemId, String sellerId, RequestListener<List<RelatedItem>> listener) {
        baseRequest((BaseMtopRequest) new GetWapRelatedItems(itemId, sellerId), listener, false);
    }

    public void getShopCoupon(String sellerId, RequestListener<List<ShopCoupon>> listener) {
        baseRequest((BaseMtopRequest) new GetShopCoupon(sellerId), listener, false);
    }

    public void applyShopCoupon(String sellerId, String spreadId, RequestListener<JSONObject> listener) {
        applyShopCoupon(sellerId, spreadId, (String) null, listener);
    }

    public void applyShopCoupon(String sellerId, String spreadId, String uuid, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new ApplyShopCoupon(sellerId, spreadId, uuid), listener, true);
    }

    public void couponReceive(CouponReceiveParamsBean couponReceiveParamsBean, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new CouponReceiveRequest(couponReceiveParamsBean), listener, true);
    }

    public void shopFavorites(CouponReceiveParamsBean couponReceiveParamsBean, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new ShopFavoritesRequest(couponReceiveParamsBean), listener, true);
    }

    public void requestTaokeLoginAnalysis(String nickName, String bizSource, String referer, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new TaokeLoginAnalysisRequest(nickName, bizSource, referer), listener, false);
    }

    public void requestTaokeDetailAnalysis(String stbId, String nickName, String tid, String sourceType, String sellerId, String bizSource, String referer, String action, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new TaokeDetailAnalysisRequest(stbId, nickName, tid, sourceType, sellerId, bizSource, referer, action), listener, false);
    }

    public void requestTaokeJHSListAnalysis(String stbId, String nickName, String bizSource, String referer, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new TaokeJHSListAnalysisRequest(stbId, nickName, bizSource, referer), listener, false);
    }

    public void requestAlimamaApplyCoupon(String itemId, String pid, String couponKey, String userId, RequestListener<String> listener) {
        baseRequest((BaseHttpRequest) new ApplyCouponRequest(itemId, pid, couponKey, userId), listener, false, true);
    }

    public void requestUpGrade(String version, String uuid, String channelId, String code, String versionCode, String versionName, String systemInfo, String umtoken, String modelInfo, String extParams, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new UpgradeApp(version, uuid, channelId, code, versionCode, versionName, systemInfo, umtoken, modelInfo, extParams), listener, false, true);
    }

    public void requestNewFeature(String version, String uuid, String channelId, String code, String versionCode, String versionName, String systemInfo, String umtoken, String modelInfo, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new UpgradeNewFeature(version, uuid, channelId, code, versionCode, versionName, systemInfo, umtoken, modelInfo), listener, false, true);
    }

    public void logReceive(String osVersion, String uuid, String channelId, String code, String versionCode, String versionName, String systemInfo, String log, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new LogReceive(osVersion, uuid, channelId, code, versionCode, versionName, systemInfo, log), listener, false, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x020c  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0285  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x03a3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> com.yunos.tvtaobao.biz.request.core.ServiceResponse<T> normalLoad(com.yunos.tvtaobao.biz.request.base.BaseMtopRequest r24, boolean r25, int r26) {
        /*
            r23 = this;
            r21 = 0
            r24.setParamsData()
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.request = "
            java.lang.StringBuilder r3 = r3.append(r4)
            r0 = r24
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r4 = ", post = "
            java.lang.StringBuilder r3 = r3.append(r4)
            r0 = r25
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            mtopsdk.mtop.intf.Mtop r2 = com.yunos.tv.core.AppInitializer.getMtopInstance()
            java.lang.String r3 = r24.getTTid()
            r0 = r24
            mtopsdk.mtop.intf.MtopBuilder r2 = r2.build((mtopsdk.mtop.domain.MtopRequest) r0, (java.lang.String) r3)
            mtopsdk.mtop.intf.MtopBuilder r15 = r2.useWua()
            mtopsdk.mtop.intf.Mtop r2 = com.yunos.tv.core.AppInitializer.getMtopInstance()
            mtopsdk.mtop.global.MtopConfig r2 = r2.getMtopConfig()
            mtopsdk.framework.manager.FilterManager r2 = r2.filterManager
            if (r2 == 0) goto L_0x005e
            com.yunos.tvtaobao.biz.request.core.CheckRespFilter r2 = com.yunos.tvtaobao.biz.request.core.CheckRespFilter.getInstance()
            mtopsdk.mtop.intf.Mtop r3 = com.yunos.tv.core.AppInitializer.getMtopInstance()
            mtopsdk.mtop.global.MtopConfig r3 = r3.getMtopConfig()
            mtopsdk.framework.manager.FilterManager r3 = r3.filterManager
            r2.inject(r3)
        L_0x005e:
            r0 = r26
            r15.setSocketTimeoutMilliSecond(r0)
            if (r25 == 0) goto L_0x006a
            mtopsdk.mtop.domain.MethodEnum r2 = mtopsdk.mtop.domain.MethodEnum.POST
            r15.reqMethod(r2)
        L_0x006a:
            r15.useWua()
            mtopsdk.mtop.domain.MtopResponse r18 = r15.syncRequest()
            if (r18 == 0) goto L_0x033d
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.String r3 = ".normalLoad.mtopResponse print ---- bgn"
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.mtopResponse = "
            java.lang.StringBuilder r3 = r3.append(r4)
            r0 = r18
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.isApiSuccess = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.isApiSuccess()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", isNetworkError = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.isNetworkError()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", isSessionInvalid = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.isSessionInvalid()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", isSystemError = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.isSystemError()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", isExpiredRequest = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.isExpiredRequest()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", is41XResult = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.is41XResult()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", isApiLockedResult = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.isApiLockedResult()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = ", isMtopSdkError = "
            java.lang.StringBuilder r3 = r3.append(r4)
            boolean r4 = r18.isMtopSdkError()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.v(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.getResponseCode = "
            java.lang.StringBuilder r3 = r3.append(r4)
            int r4 = r18.getResponseCode()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.getRetCode = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r18.getRetCode()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.getRetMsg = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r18.getRetMsg()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.getFullKey = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r18.getFullKey()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.getBytedata = "
            java.lang.StringBuilder r3 = r3.append(r4)
            byte[] r4 = r18.getBytedata()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.getApi = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r18.getApi()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.String r3 = ".normalLoad.mtopResponse print ---- end"
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            r0 = r23
            com.yunos.tvtaobao.biz.request.BusinessRequest$MtopResponseListener r2 = r0.mtopResponseListener
            if (r2 == 0) goto L_0x01f5
            r0 = r23
            com.yunos.tvtaobao.biz.request.BusinessRequest$MtopResponseListener r2 = r0.mtopResponseListener     // Catch:{ Throwable -> 0x02c7 }
            r0 = r18
            r2.onMtopResponse(r0)     // Catch:{ Throwable -> 0x02c7 }
        L_0x01f5:
            byte[] r2 = r18.getBytedata()     // Catch:{ Exception -> 0x02f5 }
            if (r2 == 0) goto L_0x02cd
            java.lang.String r2 = new java.lang.String     // Catch:{ Exception -> 0x02f5 }
            byte[] r3 = r18.getBytedata()     // Catch:{ Exception -> 0x02f5 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x02f5 }
            r0 = r24
            com.yunos.tvtaobao.biz.request.core.ServiceResponse r21 = r0.resolveResponse((java.lang.String) r2)     // Catch:{ Exception -> 0x02f5 }
        L_0x020a:
            if (r21 != 0) goto L_0x0244
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.String r3 = ".normalLoad.custom serviceResponse"
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            com.yunos.tvtaobao.biz.request.core.ServiceResponse r21 = new com.yunos.tvtaobao.biz.request.core.ServiceResponse
            r21.<init>()
            int r2 = r18.getResponseCode()
            r3 = 419(0x1a3, float:5.87E-43)
            if (r2 != r3) goto L_0x0364
            java.lang.String r2 = r18.getApi()
            java.lang.String r3 = "mtop.trade.buildOrder"
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x023d
            java.lang.String r2 = r18.getApi()
            java.lang.String r3 = "mtop.trade.buildOrder"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0364
        L_0x023d:
            com.yunos.tvtaobao.biz.request.core.ServiceCode r2 = com.yunos.tvtaobao.biz.request.core.ServiceCode.API_ERROR_HTTP
            r0 = r21
            r0.update(r2)
        L_0x0244:
            r0 = r23
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = ".normalLoad.serviceResponse = "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = r21.toString()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.zhiping.dev.android.logger.ZpLogger.i(r2, r3)
            int r2 = r18.getResponseCode()
            r3 = 419(0x1a3, float:5.87E-43)
            if (r2 != r3) goto L_0x03a3
            java.lang.String r2 = r18.getApi()
            java.lang.String r3 = "mtop.trade.buildOrder"
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0285
            java.lang.String r2 = r18.getApi()
            java.lang.String r3 = "mtop.trade.createOrder"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x03a3
        L_0x0285:
            r2 = 419(0x1a3, float:5.87E-43)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0 = r21
            r0.setStatusCode(r2)
            com.yunos.tvtaobao.payment.utils.ErrorReport r7 = com.yunos.tvtaobao.payment.utils.ErrorReport.getInstance()
            java.lang.String r8 = r24.getApiName()
            java.lang.String r9 = r24.getVersion()
            r0 = r24
            java.util.Map r10 = r0.dataParams
            java.lang.String r11 = "FILTER_ERROR"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ""
            java.lang.StringBuilder r2 = r2.append(r3)
            int r3 = r18.getResponseCode()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r12 = r2.toString()
            java.lang.String r13 = r18.getRetCode()
            java.lang.String r14 = r18.getRetMsg()
            r7.uploadMtopError(r8, r9, r10, r11, r12, r13, r14)
        L_0x02c6:
            return r21
        L_0x02c7:
            r16 = move-exception
            r16.printStackTrace()
            goto L_0x01f5
        L_0x02cd:
            java.lang.String r19 = r18.getRetCode()     // Catch:{ Exception -> 0x02f5 }
            boolean r2 = android.text.TextUtils.isEmpty(r19)     // Catch:{ Exception -> 0x02f5 }
            if (r2 != 0) goto L_0x020a
            com.yunos.tvtaobao.biz.request.core.ServiceCode r20 = com.yunos.tvtaobao.biz.request.core.ServiceCode.valueOf(r19)     // Catch:{ Exception -> 0x02f5 }
            if (r20 == 0) goto L_0x020a
            com.yunos.tvtaobao.biz.request.core.ServiceResponse r22 = new com.yunos.tvtaobao.biz.request.core.ServiceResponse     // Catch:{ Exception -> 0x02f5 }
            r22.<init>()     // Catch:{ Exception -> 0x02f5 }
            int r2 = r20.getCode()     // Catch:{ Exception -> 0x03e1 }
            java.lang.String r3 = r20.getMsg()     // Catch:{ Exception -> 0x03e1 }
            r0 = r22
            r1 = r19
            r0.update(r2, r1, r3)     // Catch:{ Exception -> 0x03e1 }
            r21 = r22
            goto L_0x020a
        L_0x02f5:
            r17 = move-exception
        L_0x02f6:
            com.yunos.tvtaobao.biz.request.core.ServiceResponse r21 = new com.yunos.tvtaobao.biz.request.core.ServiceResponse
            r21.<init>()
            com.yunos.tvtaobao.biz.request.core.ServiceCode r2 = com.yunos.tvtaobao.biz.request.core.ServiceCode.DATA_PARSE_ERROR
            r0 = r21
            r0.update(r2)
            com.yunos.tvtaobao.payment.utils.ErrorReport r2 = com.yunos.tvtaobao.payment.utils.ErrorReport.getInstance()
            java.lang.String r3 = r24.getApiName()
            java.lang.String r4 = r24.getVersion()
            r0 = r24
            java.util.Map r5 = r0.dataParams
            java.lang.String r6 = "PARSE_ERROR"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = ""
            java.lang.StringBuilder r7 = r7.append(r8)
            int r8 = r18.getResponseCode()
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            java.lang.String r8 = r18.getRetCode()
            java.lang.String r9 = r18.getRetMsg()
            r2.uploadMtopError(r3, r4, r5, r6, r7, r8, r9)
            r17.printStackTrace()
            goto L_0x020a
        L_0x033d:
            com.yunos.tvtaobao.biz.request.core.ServiceResponse r21 = new com.yunos.tvtaobao.biz.request.core.ServiceResponse
            r21.<init>()
            com.yunos.tvtaobao.biz.request.core.ServiceCode r2 = com.yunos.tvtaobao.biz.request.core.ServiceCode.HTTP_ERROR
            r0 = r21
            r0.update(r2)
            java.lang.String r6 = "HTTP_ERROR"
            com.yunos.tvtaobao.payment.utils.ErrorReport r2 = com.yunos.tvtaobao.payment.utils.ErrorReport.getInstance()
            java.lang.String r3 = r24.getApiName()
            java.lang.String r4 = r24.getVersion()
            r0 = r24
            java.util.Map r5 = r0.dataParams
            r7 = 0
            r8 = 0
            r9 = 0
            r2.uploadMtopError(r3, r4, r5, r6, r7, r8, r9)
            goto L_0x020a
        L_0x0364:
            com.yunos.tvtaobao.biz.request.core.ServiceCode r2 = com.yunos.tvtaobao.biz.request.core.ServiceCode.API_ERROR
            r0 = r21
            r0.update(r2)
            com.yunos.tvtaobao.payment.utils.ErrorReport r7 = com.yunos.tvtaobao.payment.utils.ErrorReport.getInstance()
            java.lang.String r8 = r24.getApiName()
            java.lang.String r9 = r24.getVersion()
            r0 = r24
            java.util.Map r10 = r0.dataParams
            java.lang.String r11 = "API_ERROR"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ""
            java.lang.StringBuilder r2 = r2.append(r3)
            int r3 = r18.getResponseCode()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r12 = r2.toString()
            java.lang.String r13 = r18.getRetCode()
            java.lang.String r14 = r18.getRetMsg()
            r7.uploadMtopError(r8, r9, r10, r11, r12, r13, r14)
            goto L_0x0244
        L_0x03a3:
            boolean r2 = r18.isApiSuccess()
            if (r2 != 0) goto L_0x02c6
            com.yunos.tvtaobao.payment.utils.ErrorReport r7 = com.yunos.tvtaobao.payment.utils.ErrorReport.getInstance()
            java.lang.String r8 = r24.getApiName()
            java.lang.String r9 = r24.getVersion()
            r0 = r24
            java.util.Map r10 = r0.dataParams
            java.lang.String r11 = "API_ERROR"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ""
            java.lang.StringBuilder r2 = r2.append(r3)
            int r3 = r18.getResponseCode()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r12 = r2.toString()
            java.lang.String r13 = r18.getRetCode()
            java.lang.String r14 = r18.getRetMsg()
            r7.uploadMtopError(r8, r9, r10, r11, r12, r13, r14)
            goto L_0x02c6
        L_0x03e1:
            r17 = move-exception
            r21 = r22
            goto L_0x02f6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.request.BusinessRequest.normalLoad(com.yunos.tvtaobao.biz.request.base.BaseMtopRequest, boolean, int):com.yunos.tvtaobao.biz.request.core.ServiceResponse");
    }

    /* access modifiers changed from: private */
    public <T> void normalPostExecute(ServiceResponse<T> result, RequestListener<T> listener) {
        if (listener != null) {
            try {
                listener.onRequestDone(result.getData(), result.getStatusCode().intValue(), result.getErrorMsg());
            } catch (Throwable e) {
                if (!DisasterTolerance.getInstance().catchRequestDoneException(e, new String[0])) {
                    throw e;
                }
            }
        }
    }

    public <T> ServiceResponse<T> processHttpRequest(BaseHttpRequest theRequest, boolean post) {
        String url;
        String statusCode;
        ServiceResponse<T> resolveResponse;
        Network network = new DegradableNetwork(CoreApplication.getApplication());
        if (post) {
            url = theRequest.getPostUrl();
        } else {
            url = theRequest.getUrl();
        }
        ZpLogger.v(this.TAG, ".processHttpRequest.url = " + url + ", post = " + post);
        Response response = null;
        if (!TextUtils.isEmpty(url)) {
            RequestImpl request = new RequestImpl(url);
            if (!(theRequest == null || theRequest.getHttpHeader() == null || theRequest.getHttpHeader().isEmpty())) {
                List<Header> headers = theRequest.getHttpHeader();
                List<anetwork.channel.Header> aHeaders = new ArrayList<>();
                for (Header header : headers) {
                    aHeaders.add(new HeaderWrapper(header));
                }
                request.setHeaders(aHeaders);
            }
            if (post) {
                request.setMethod("POST");
                request.setParams(theRequest.getPostParametersForRequestImpl());
            }
            try {
                response = network.syncSend(request, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorReport.getInstance().uploadHttpError(theRequest.getUrl(), ErrorReport.ERRORTYPE_EXCEPTION, (String) null, (String) null);
            }
            ZpLogger.v(this.TAG, ".processHttpRequest.request = " + request);
            ZpLogger.v(this.TAG, ".processHttpRequest.response = " + response);
        }
        byte[] data = null;
        if (response != null) {
            data = response.getBytedata();
        }
        if (response == null) {
            statusCode = null;
        } else {
            statusCode = response.getStatusCode() + "";
        }
        if (data != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), theRequest.getResponseEncode()));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String inputLine = reader.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    sb.append(inputLine);
                }
                resolveResponse = theRequest.resolveResponse(sb.toString());
            } catch (Exception e2) {
                resolveResponse = new ServiceResponse<>();
                resolveResponse.update(ServiceCode.DATA_PARSE_ERROR);
                ErrorReport.getInstance().uploadHttpError(theRequest.getUrl(), ErrorReport.ERRORTYPE_PARSEERROR, statusCode, (String) null);
                e2.printStackTrace();
            }
        } else {
            resolveResponse = new ServiceResponse<>();
            resolveResponse.update(ServiceCode.HTTP_ERROR);
            ErrorReport.getInstance().uploadHttpError(theRequest.getUrl(), ErrorReport.ERRORTYPE_HTTPERROR, statusCode, (String) null);
        }
        if (resolveResponse != null && !resolveResponse.isSucess()) {
            String errorCode = resolveResponse.getErrorCode();
            String errorMsg = resolveResponse.getErrorMsg();
            ErrorReport.getInstance().uploadHttpError(theRequest.getUrl(), ErrorReport.ERRORTYPE_APIERROR, statusCode, (!TextUtils.isEmpty(errorCode) || !TextUtils.isEmpty(errorMsg)) ? errorCode + "|" + errorMsg : null);
        }
        ZpLogger.v(this.TAG, ".processHttpRequest.resolveResponse = " + resolveResponse);
        return resolveResponse;
    }

    public void requestWeitaoFollow(String accountType, String pubAccountId, RequestListener<WeitaoFollowBean> listener) {
        baseRequest((BaseMtopRequest) new WeitaoFollowRequest(accountType, pubAccountId), listener, true);
    }

    public void requestWeitaoRemove(String pubAccountId, String originPage, String originFlag, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new WeitaoRemoveRequest(pubAccountId, originPage, originFlag), listener, true);
    }

    public void requestWeitaoAdd(String accountType, String pubAccountId, String originPage, String originFlag, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new WeitaoAddRequest(accountType, pubAccountId, originPage, originFlag), listener, true);
    }

    public void requestWeitaoSetDynamic(String pubAccountId, boolean status, RequestListener<JSONObject> listener) {
        baseRequest((BaseMtopRequest) new WeitaoSetDynamic(pubAccountId, status), listener, true);
    }

    public void requestShopHomeDetail(String shopId, String serviceId, String longitude, String latitude, String extFeature, String pageNo, String genreIds, RequestListener<ShopDetailData> listener) {
        baseRequest((BaseMtopRequest) new ShopDetailDataRequest(shopId, serviceId, longitude, latitude, extFeature, pageNo, genreIds), listener, false);
    }

    public void requestSearchResult(String shopId, String keyword, String orderType, int pageSize, int pageNo, RequestListener<ShopSearchResultBean> listener) {
        baseRequest((BaseMtopRequest) new ShopSearchRequest(shopId, keyword, orderType, pageSize, pageNo), listener, false);
    }

    public void requestTakeOutAddBag(String itemId, String skuId, String quantity, String exParams, String cartFrom, RequestListener<AddBagBo> listener) {
        baseRequest(new TakeOutAddBagRequest(itemId, skuId, quantity, exParams, cartFrom), listener, false, true, 0);
    }

    public void requestTakeOutAgain(String storeId, String orderItems, RequestListener<TakeOutBagAgain> listener) {
        baseRequest(new TakeOutAgainRequest(storeId, orderItems), listener, true, false, 0);
    }

    public void requestTakeOutUpdate(String storeId, String latitude, String longitude, String operateType, String paramList, RequestListener<TakeOutBag> listener) {
        baseRequest(new TakeOutUpdateBagRequest(storeId, latitude, longitude, operateType, paramList), listener, true, false, 0);
    }

    public void getTakeOutBag(String storeId, String longitude, String latitude, String extFeature, RequestListener<TakeOutBag> listener) {
        baseRequest(new TakeOutGetBagRequest(storeId, longitude, latitude, extFeature), listener, true, false, 0);
    }

    public void requestTakeOutVouchers(String storeId, RequestListener<VouchersMO> listener) {
        baseRequest((BaseMtopRequest) new TakeOutVouchersRequest(storeId), listener, false);
    }

    public void requestTakeOutVouchersList(String storeId, RequestListener<VouchersList> listener) {
        baseRequest((BaseMtopRequest) new TakeOutVouchersListRequest(storeId), listener, true);
    }

    public void requestGetTakeOutVouchers(String storeId, String activityId, String exchangeType, String storeIdType, RequestListener<TakeVouchers> listener) {
        baseRequest((BaseMtopRequest) new TakeOutGetVouchersRequest(storeId, activityId, exchangeType, storeIdType), listener, true);
    }

    public void requestTakeOutVouchersSummary(String storeId, RequestListener<VouchersSummary> listener) {
        baseRequest((BaseMtopRequest) new TakeOutVouchersSummaryRequest(storeId), listener, false);
    }

    public void requestTakeOutApplyCoupon(String asac, String channel, RequestListener<TakeoutApplyCoupon> listener) {
        baseRequest((BaseMtopRequest) new TakeoutApplyCouponRequest(asac, channel), listener, false);
    }

    public void requestTakeOutCouponStyle(RequestListener<TakeOutCouponStyle> listener) {
        baseRequest((BaseMtopRequest) new TakeOutCouponStyleRequest(), listener, false);
    }

    public void requestSuperChoiceJhsList(int catId, int pageNum, int pageSize, boolean isSuperChoice, RequestListener<JhsSuperChoiceGoodsBean> listener) {
        baseRequest((BaseMtopRequest) new GetSuperChoiceJhsListRequest(catId, pageNum, pageSize, isSuperChoice), listener, false);
    }

    public void requestNormalJhsList(int catId, int pageNum, int pageSize, boolean isSuperChoice, RequestListener<JhsNormalGoodsBean> listener) {
        baseRequest((BaseMtopRequest) new GetNormalJhsListRequest(catId, pageNum, pageSize, isSuperChoice), listener, false);
    }

    public void requestFootMarkSummation(RequestListener<GetFootMarkSummationResponse> listener) {
        baseRequest((BaseMtopRequest) new GetFootMarkSummationRequest(), listener, true);
    }

    public void requestFootMarkData(long endtime, RequestListener<GetFootMarkResponse> listener) {
        baseRequest((BaseMtopRequest) new GetFootMarkRequest(endtime), listener, true);
    }

    public void requestFollowData(boolean isShop, boolean isDaRen, String cursor, RequestListener<FollowData> listener) {
        baseRequest((BaseMtopRequest) new GetFollowRequest(isShop, isDaRen, cursor), listener, true);
    }

    public void addFollowRequest(String followedId, RequestListener<AddFollowResult> listener) {
        baseRequest((BaseMtopRequest) new AddFollowRequest(followedId), listener, true);
    }

    public void removeFollowRequest(String followedId, RequestListener<String> listener) {
        baseRequest((BaseMtopRequest) new RemoveFollowRequest(followedId), listener, true);
    }

    public void getMyFollowDaRenRequest(String cursor, String pageSize, RequestListener<MyFollowBean> listener) {
        baseRequest((BaseMtopRequest) new GetMyFollowRequest("false", "true", cursor, pageSize), listener, true);
    }

    public void getMyFollowShopRequest(String cursor, String pageSize, RequestListener<MyFollowBean> listener) {
        baseRequest((BaseMtopRequest) new GetMyFollowRequest("true", "false", cursor, pageSize), listener, true);
    }

    public void getByTaobaoUserId(RequestListener<ElemBindBo> listener) {
        baseRequest((BaseMtopRequest) new GetByTaobaoUserIdRequest(), listener, true);
    }

    public void getOauthUrl(String request, RequestListener<ElemeOauthBo> listener) {
        baseRequest((BaseMtopRequest) new GetOauthUrlRequest(request), listener, true);
    }

    public void getElemeRecommand(String requestToken, RequestListener<ElemeRecommandBo> listener) {
        baseRequest((BaseMtopRequest) new GetElemeRecommandRequest(requestToken), listener, true);
    }

    public void getElemeRecommandBind(String request, RequestListener<ElemeOauthBo> listener) {
        baseRequest((BaseMtopRequest) new GetElemeRecommandBindRequest(request), listener, true);
    }

    public void getElemeTaobaoPhone(String requestToken, RequestListener<ElemeOauthBo> listener) {
        baseRequest((BaseMtopRequest) new GetElemeTaobaoPhoneRequest(requestToken), listener, true);
    }

    public void getElemeRegisterAndBind(String requestToken, RequestListener<ElemeOauthBo> listener) {
        baseRequest((BaseMtopRequest) new GetElemeRegisterAndBindRequest(requestToken), listener, true);
    }

    private class HeaderWrapper implements anetwork.channel.Header {
        private Header mHeader;

        HeaderWrapper(Header header) {
            this.mHeader = header;
        }

        public String getName() {
            return this.mHeader.getName();
        }

        public String getValue() {
            return this.mHeader.getValue();
        }
    }
}
