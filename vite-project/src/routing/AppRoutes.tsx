import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "../App";
import { MasterLayout } from "../layouts/MasterLayout";
import type { FC } from "react";
import Login from "../pages/login";
import Info from "../pages/info";
import ListChat from "../pages/listChat";
import ListFriends from "../pages/listFriends";
import Setting from "../pages/setting";

const AppRoutes: FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<App />}>
                    <Route path="/" element={
                        <Login />
                    } />
                    <Route element={<MasterLayout />}>
                        <Route path="info" element={<Info />}/>
                        <Route path="chat" element={<ListChat />} />
                        <Route path="contacts" element={<ListFriends />} />
                        <Route path="settings" element={<Setting />} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export { AppRoutes };