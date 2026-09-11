import { Card } from "@/components/ui/card";
import PageHeader from "../page-header";
import { AlertCircle, RefreshCw } from "lucide-react";
import { useTranslation } from "react-i18next";
import { Button } from "@/components/ui/button";

interface Props {
  handleRefresh: () => void;
}

export function DataTableFetchError({ handleRefresh }: Props) {
  const { t } = useTranslation();
  return (
    <>
      <PageHeader title={t("data_fetch_error_header")} />
      <Card className="p-8">
        <div className="flex flex-col items-center justify-center text-center">
          <div className="rounded-full bg-destructive/10 p-4">
            <AlertCircle className="h-8 w-8 text-destructive" />
          </div>
          <h3 className="mt-4 text-lg font-semibold">
            {t("data_fetch_error_title")}
          </h3>
          <p className="mt-2 max-w-md text-sm text-muted-foreground">
            {t("error_fetch_data")}
          </p>
          <Button
            variant="outline"
            className="mt-4 gap-2"
            onClick={handleRefresh}
          >
            <RefreshCw className="h-4 w-4" />
            {t("retry")}
          </Button>
        </div>
      </Card>
    </>
  );
}
