package ru.ksu.motygullin.gallery.network;

import android.support.annotation.NonNull;

import ru.ksu.motygullin.gallery.VkFriend;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * @author Rishad Mustafaev
 */

public class RxVk {

    public void getUsers(final RxVkListener<VKList<VKApiUser>> listener, long userId) {
        Subscriber<VKList<VKApiUser>> usersSubscriber = Subscribers.create(new Action1<VKList<VKApiUser>>() {
            @Override
            public void call(VKList<VKApiUser> vkApiUsers) {
                listener.requestFinished(vkApiUsers);
            }
        });
        pGetUsers(userId).subscribe(usersSubscriber);
    }

    public void getFriends(final RxVkListener<List<VkFriend>> listener) {
        Subscriber<List<VkFriend>> friendsSubscriber = Subscribers.create(new Action1<List<VkFriend>>() {
            @Override
            public void call(List<VkFriend> vkApiUsers) {
                listener.requestFinished(vkApiUsers);
            }
        });
        pGetFriends(VKParameters.from("order", "hints", "fields", "name, photo_50, photo_max_orig, online"))
                .subscribe(friendsSubscriber);

    }


    private Observable<VKList<VKApiUser>> pGetUsers(final long userId) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(final AsyncEmitter<VKResponse> vkResponseAsyncEmitter) {
                VKParameters parameters = userId != 0 ? VKParameters.from(VKApiConst.USER_IDS, userId)
                        : null;
                VKApi.users().get(parameters).executeWithListener(new RxVkRequestListener(vkResponseAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // do nothing
                    }
                })
                .flatMap(new Func1<VKResponse, Observable<VKList<VKApiUser>>>() {
                    @Override
                    public Observable<VKList<VKApiUser>> call(VKResponse vkResponse) {
                        Object parsedModel = vkResponse.parsedModel;
                        if (parsedModel != null && parsedModel instanceof VKList) {
                            VKList list = ((VKList) parsedModel);
                            if (checkCast(VKApiUser.class, list)) {
                                //noinspection unchecked
                                return Observable.just(((VKList<VKApiUser>) parsedModel));
                            }
                        }
                        return Observable.empty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<VkFriend>> pGetFriends(final VKParameters params) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<VKResponse>>() {
            @Override
            public void call(AsyncEmitter<VKResponse> vkResponseAsyncEmitter) {
                VKApi.friends().get(params)
                        .executeWithListener(new RxVkRequestListener(vkResponseAsyncEmitter));
            }
        }, AsyncEmitter.BackpressureMode.LATEST)
                .flatMap(new Func1<VKResponse, Observable<List<VkFriend>>>() {
                    @Override
                    public Observable<List<VkFriend>> call(VKResponse vkResponse) {
                        Object parsedModel = vkResponse.parsedModel;
                        if (parsedModel != null && parsedModel instanceof VKList) {
                            VKList list = ((VKList) parsedModel);
                            if (checkCast(VKApiUser.class, list)) {
                                //noinspection unchecked
                                VKList<VKApiUser> castedList = ((VKList<VKApiUser>) list);
                                List<VkFriend> friends = new ArrayList<>();
                                for (VKApiUser user : castedList) {
                                    VkFriend friend = new VkFriend();
                                    friend.setName(user.first_name);
                                    friend.setSurname(user.last_name);
                                    friend.setOnline(user.online);
                                    friend.setSmallPhotoUrl(user.photo_50);
                                    friend.setMaxPhotoUrl(user.photo_max_orig);
                                    friends.add(friend);
                                }
                                return Observable.just(friends);
                            }
                        }
                        return Observable.empty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private boolean checkCast(Class classz, @NonNull VKList list) {
        return !list.isEmpty() && list.get(0) != null && (classz.isInstance(list.get(0)));
    }

    public interface RxVkListener<T> {
        void requestFinished(T requestResult);
    }


}
