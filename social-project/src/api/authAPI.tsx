import { AuthAPI, GetInfoAPI } from "./index"; // URL base API

export interface LoginResponse {
    data: {
        token: string | null;
        authentication: boolean;
    };
    message: string;
    statusCode: number;
    success: boolean;
}

export interface UserInfo {
    id: string;
    username: string;
    email: string;
    phoneNumber: string | null;
    lastSeen: string | null;
    createdAt: string | null;
    lastLoginAt: string | null;
    status: string;
    profile: {
        fullName: string;
        avatarUrl: string;
        coverPhotoUrl: string;
        bio: string;
        gender: string;
        birthday: string | null;
        location: string;
        website: string;
    };
    provider: {
        provider: string;
        providerId: string;
    };
    settings: {
        theme: string;
        language: string;
        soundOn: boolean;
        notificationsEnabled: boolean;
    };
    online: boolean;
}

export interface GetMyInfoResponse {
    data: UserInfo;
    message: string;
    statusCode: number;
    success: boolean;
}

export const login = async (email: string, password: string): Promise<LoginResponse> => {
    const response = await fetch(`${AuthAPI}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            username: email,
            password: password,
        }),
        credentials: "include" // Quan trọng nếu server lưu token vào cookie
    });

    const data: LoginResponse = await response.json();

    if (!response.ok || !data.success || !data.data.authentication) {
        throw new Error(data.message || "Login failed");
    }

    return data;
};

export const getMyInfo = async (): Promise<GetMyInfoResponse> => {
    const response = await fetch(GetInfoAPI, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include" // gửi cookie/session kèm request
    });

    const data: GetMyInfoResponse = await response.json();

    if (!response.ok || !data.success) {
        throw new Error(data.message || "Failed to fetch user info");
    }

    return data;
};
