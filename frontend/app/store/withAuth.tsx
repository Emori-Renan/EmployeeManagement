"use client"
import { useSelector } from 'react-redux';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

const withAuth = (WrappedComponent) => {
  const EnhancedComponent = (props) => {
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    const router = useRouter();

    useEffect(() => {
      console.log("saporra ta certa? deve estar false: ",isAuthenticated);
      console.log("olha o otken ae: ", localStorage.getItem('token'));
      
      
      if (!isAuthenticated) {
        router.push(`/login?redirect=${encodeURIComponent(window.location.pathname)}`); // Redirect to login if not authenticated
      }
    }, [isAuthenticated, router]);

    // While redirecting, avoid rendering the protected component
    if (!isAuthenticated) {
      return null;
    }

    // Render the wrapped component with its original props
    return <WrappedComponent {...props} />;
  };

  // Add a display name for easier debugging
  EnhancedComponent.displayName = `WithAuth(${WrappedComponent.displayName || WrappedComponent.name || 'Component'})`;

  return EnhancedComponent;
};

export default withAuth;
