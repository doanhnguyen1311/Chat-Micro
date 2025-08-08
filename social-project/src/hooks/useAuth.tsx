import { useState, useEffect, useCallback } from "react";
import { login as loginApi, getMyInfo } from "../api/authAPI";
import type { UserInfo } from "../api/authAPI";

export function useAuth() {
    const [user, setUser] = useState<UserInfo | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    // Hàm login
    const login = useCallback(async (email: string, password: string) => {
        await loginApi(email, password);
        const userInfo = await getMyInfo();
        setUser(userInfo.data);
        return userInfo.data;
    }, []);

    // Hàm load user info nếu đã login (cookie/session vẫn còn)
    const fetchMyInfo = useCallback(async () => {
        try {
            const userInfo = await getMyInfo();
            setUser(userInfo.data);
        } catch {
            setUser(null);
        } finally {
            setLoading(false);
        }
    }, []);

    // Chạy khi component đầu tiên mount để lấy user info
    useEffect(() => {
        fetchMyInfo();
    }, [fetchMyInfo]);

    return {
        user,
        loading,
        login,
        getMyInfo: fetchMyInfo,
        isAuthenticated: !!user
    };
}
