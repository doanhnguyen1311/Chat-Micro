import { Suspense } from 'react';
import '../src/assets/css/GlobalStyles.scss';
import './App.css';
import { Outlet } from 'react-router-dom';

function App() {
  return (
    <Suspense>
      <Outlet />
    </Suspense>
  );
}

export default App;
