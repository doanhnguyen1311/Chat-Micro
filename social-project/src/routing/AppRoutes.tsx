import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "../App";
import type { FC } from "react";
import { MasterLayout } from "../layouts/MasterLayout";
import { AuthLayout } from "../layouts/AuthLayout";
import Login from "../pages/login";
import NewsFeed from "../pages/newsfeed";
import Watch from "../pages/watch";
import People from "../pages/people";
import GroupPage from "../pages/group";

const AppRoutes: FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<App />}>
                    <Route path="/" element={
                        <Login />
                    } />
                    <Route element={<MasterLayout />}>
                        <Route path="feeds" element={<NewsFeed />} />
                        <Route path="watch" element={<Watch />} />
                        <Route path="people" element={<People />} />
                        <Route path="groups" element={<GroupPage />} />
                    </Route>
                    <Route element={<AuthLayout />}>

                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export { AppRoutes };